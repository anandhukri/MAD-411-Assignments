package com.example.assignment7

interface QuoteApiService {
        @GET("random")
        suspend fun getQuotes(): List<Quote>
        abstract fun getExchangeRates(s: String): Any
}