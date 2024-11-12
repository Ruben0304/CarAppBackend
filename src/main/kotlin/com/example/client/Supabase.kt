package com.example.client

import io.github.cdimascio.dotenv.Dotenv
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.serializer.KotlinXSerializer
import kotlinx.serialization.json.Json

fun connectToSupabase(dotenv: Dotenv): SupabaseClient{
    val supabaseUrl = dotenv["DB_SUPABASE_URL"]
    val supabaseKey =  dotenv["DB_SUPABASE_KEY"]

   return createSupabaseClient(supabaseUrl, supabaseKey) {
        defaultSerializer = KotlinXSerializer(Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = true
            prettyPrint = true
        })
       install(Auth)
    }

}