package com.example.di

import com.example.implementations.*
import com.example.implementations.dao.CarroServiceImpl
import com.example.implementations.dao.ConversationServiceImpl
import com.example.implementations.dao.MessageServiceImpl
import com.example.implementations.dao.PiezaServiceImpl
import com.example.services.*
import com.example.services.dao.CarroService
import com.example.services.dao.ConversationService
import com.example.services.dao.MessageService
import com.example.services.dao.PiezaService
import org.koin.core.module.Module
import org.koin.dsl.module

fun serviceModules(): Module {
   return module {
       single<AuthService>{SupabaseAuthImpl()}
       single<PiezaService>{ PiezaServiceImpl() }
       single<CarroService>{ CarroServiceImpl() }
       single<ConversationService>{ ConversationServiceImpl() }
       single<MessageService>{ MessageServiceImpl() }
       single<EmbeddingService>{ EmbeddingServiceImpl() }
       single<SpanishTransformerService>{SpanishTransformerImpl()}
       single<VectorialDatabaseService>{VectorialDatabaseImpl()}
       single<VectorialSearchService>{VectorialSearchImpl()}
    }
}
