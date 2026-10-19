package ru.student.weather51

// Имена полей соответствуют ключам JSON OpenWeatherMap.
data class WeatherResponse(
    val name: String?,
    val main: MainWeather?,
    val wind: Wind?,
    val weather: List<WeatherDescription>?
)
data class MainWeather(val temp: Double?, val pressure: Int?)
data class Wind(val speed: Double?)
data class WeatherDescription(val description: String?)
