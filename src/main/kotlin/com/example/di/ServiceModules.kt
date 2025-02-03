package com.example.di

import com.example.implementations.*
import com.example.implementations.dao.*
import com.example.services.*
import com.example.services.dao.*
import org.koin.core.module.Module
import org.koin.dsl.module

fun serviceModules(): Module {
   return module {
       single<AuthService>{SupabaseAuthImpl()}
       single<PiezaService>{ PiezaServiceImpl() }
       single<CarroService>{ CarroServiceImpl() }
       single<ConversationService>{ ConversationServiceImpl() }
       single<MessageService>{ MessageServiceImpl() }
       single<UserService>{ UserServiceImpl() }
       single<EmbeddingService>{ EmbeddingServiceImpl() }
       single<SpanishTransformerService>{SpanishTransformerImpl()}
       single<VectorialDatabaseService>{VectorialDatabaseImpl()}
       single<VectorialSearchService>{VectorialSearchImpl()}
    }
}
