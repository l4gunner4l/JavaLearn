package ru.l4gunner4l.javalearn

import org.junit.Assert
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        Assert.assertEquals(4, 2 + 2.toLong())
    }
    @Test
    fun test7(){
        var a = 0
        for(i in 0 until 6) {
            if (a==8){
                print("bye ")
                continue
            }
            a += 2
        }
        print("a="+a)
    }
}