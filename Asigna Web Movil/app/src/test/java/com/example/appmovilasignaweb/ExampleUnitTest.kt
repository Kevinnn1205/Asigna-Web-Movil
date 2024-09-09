package com.example.appmovilasignaweb

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(12, suma(5,7))
    }


    fun suma( num:Int, num2:Int):Int{
        return num+num2
    }
}