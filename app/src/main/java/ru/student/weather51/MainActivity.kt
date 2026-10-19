package ru.student.weather51

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import ru.student.weather51.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var queue: RequestQueue
    private val gson = Gson()
    private var lastJson: String? = null
    private var lastSource = ""
    private val requestTag = "weather"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        queue = Volley.newRequestQueue(applicationContext)
        binding.loadButton.setOnClickListener { loadWeather() }
        binding.demoButton.setOnClickListener {
            queue.cancelAll(requestTag)
            setLoading(false)
            val json = assets.open("weather_demo.json").bufferedReader().use { it.readText() }
            showWeather(json, "ДЕМО: учебные данные, не текущая погода")
        }
        savedInstanceState?.getString("json")?.let {
            showWeather(it, savedInstanceState.getString("source").orEmpty())
        }
    }

    // Volley выполняет запрос вне основного потока; результат приходит в основной поток.
    private fun loadWeather() {
        val city = binding.cityInput.text.toString().trim()
        val key = binding.keyInput.text.toString().trim()
        if (city.isEmpty()) {
            binding.cityInput.error = "Введите название города"
            return
        }
        if (key.isEmpty()) {
            binding.keyInput.error = "Введите свой API-ключ или используйте Демо"
            return
        }
        val url = Uri.parse("https://api.openweathermap.org/data/2.5/weather")
            .buildUpon().appendQueryParameter("q", city)
            .appendQueryParameter("appid", key)
            .appendQueryParameter("units", "metric")
            .appendQueryParameter("lang", "ru").build().toString()
        queue.cancelAll(requestTag)
        setLoading(true)
        binding.statusText.text = "Загрузка погоды…"
        val request = StringRequest(Request.Method.GET, url, { json ->
            setLoading(false)
            showWeather(json, "Данные получены из OpenWeatherMap")
        }, { error ->
            setLoading(false)
            val message = when (error.networkResponse?.statusCode) {
                401 -> "Ключ неверный или ещё не активирован. Проверьте OpenWeatherMap."
                404 -> "Город не найден. Проверьте название или добавьте код страны, например Moscow,RU."
                429 -> "Превышен лимит API. Повторите запрос позже."
                null -> "Не удалось подключиться. Проверьте интернет и повторите запрос."
                else -> "Ошибка сервера. Попробуйте позже."
            }
            binding.statusText.text = "$message Предыдущие данные на экране не обновлены."
            Log.d("WeatherAPI", "Ошибка запроса, HTTP: ${error.networkResponse?.statusCode ?: "нет ответа"}")
        })
        request.tag = requestTag
        request.retryPolicy = DefaultRetryPolicy(15000, 0, 1f)
        request.setShouldCache(false)
        queue.add(request)
    }

    private fun showWeather(json: String, source: String) {
        try {
            val result = gson.fromJson(json, WeatherResponse::class.java)
            val name = requireNotNull(result.name).also { require(it.isNotBlank()) }
            val temp = requireNotNull(result.main?.temp).also { require(it.isFinite()) }
            val pressure = requireNotNull(result.main?.pressure).also { require(it > 0) }
            val wind = requireNotNull(result.wind?.speed).also { require(it.isFinite() && it >= 0) }
            binding.cityText.text = name
            binding.temperatureText.text = String.format(Locale("ru"), "%.1f °C", temp)
            binding.descriptionText.text = result.weather?.firstOrNull()?.description ?: "Без описания"
            binding.pressureText.text = "Давление: $pressure гПа"
            binding.windText.text = String.format(Locale("ru"), "Ветер: %.1f м/с", wind)
            binding.statusText.text = source
            lastJson = json
            lastSource = source
            // Фильтр Logcat: tag:WeatherAPI level:DEBUG. API-ключ не выводится.
            Log.d("WeatherAPI", "$source; город: $name; температура: $temp °C; давление: $pressure гПа; ветер: $wind м/с")
        } catch (e: Exception) {
            binding.statusText.text = "Не удалось разобрать JSON. Предыдущие данные не обновлены."
            Log.d("WeatherAPI", "Ошибка формата JSON: ${e.javaClass.simpleName}")
        }
    }

    private fun setLoading(loading: Boolean) {
        binding.progress.visibility = if (loading) View.VISIBLE else View.GONE
        binding.loadButton.isEnabled = !loading
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("json", lastJson)
        outState.putString("source", lastSource)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        queue.cancelAll(requestTag)
        queue.stop()
        super.onDestroy()
    }
}
