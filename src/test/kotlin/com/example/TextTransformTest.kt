package com.example

import com.alibaba.fastjson2.toJSONB
import com.alibaba.fastjson2.toJSONString
import org.apache.lucene.analysis.es.SpanishAnalyzer
import org.junit.Test

class TextTransformTest {
    @Test
    fun printSpanishStopWords() {
        val analyzer = SpanishAnalyzer.getDefaultStopSet()
        println(analyzer)
        assert(true)
    }
}