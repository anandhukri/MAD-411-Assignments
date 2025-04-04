import com.example.assignment7.QuoteApiService
import retrofitInstace.Retrofit
import retrofitInstance.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://zenquotes.io/api/"

    val api: QuoteApiService by lazy {
        RetrofitInstance.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuoteApiService::class.java)
    }
}
