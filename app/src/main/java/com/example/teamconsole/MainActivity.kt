package com.example.teamconsole

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread
import kotlin.coroutines.suspendCoroutine

class MainActivity : AppCompatActivity() {
    private val tag = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(tag, "Start on create")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*        thread {
                    runBlocking {
                        simpleCallback(1, 11)
                    }
                }*/
        val btnTest = findViewById<Button>(R.id.btnTest)

        btnTest.setOnClickListener {
            lifecycleScope.launch {
                simpleCallback(1, 19)
            }
        }

        Log.i(tag, "Finish on create.")
    }

    private fun List<Int>.myRandomFilter(predicate: (Int, Double) -> Boolean): List<Int> {
        val rn = random().rangeTo(3).first;
        val list = mutableListOf<Int>()
        for (n in this)
            if (predicate(n - rn, 12.0))
                list.add(n)

        return list
    }

    private suspend fun simpleCallback(a: Int, b: Int) = coroutineScope {
        var task1 = async {
            for (i in a..b) {
                delay(1000)
                Log.i(tag, "task1: Message for int= $i")
            }
        }
        var task2 = async {
            for (i in a..b) {
                delay(1000)
                Log.i(tag, "task2: Message for int= $i")
            }
        }
        var task3 = async {
            for (i in a..b) {
                delay(1000)
                Log.i(tag, "task3: Message for int= $i")
            }
        }
    }


    private fun printCountries() {
        runBlocking {
            simpleCallback(1, 5)
            getCountries().collect {
                Log.i(tag, it)
            }
        }
    }

    private fun getCountries(): Flow<String> = flow {
        for (c in getCountryService()) {
            delay(1000)
            emit(c)
        }
    }

    private fun getCountryService(): Array<String> {
        return arrayOf("Mexico", "EUA", "Italia", "Brazil")
    }


}
