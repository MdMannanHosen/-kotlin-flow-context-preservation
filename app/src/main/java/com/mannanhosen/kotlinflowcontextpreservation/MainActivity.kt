package com.mannanhosen.kotlinflowcontextpreservation
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mannanhosen.kotlinflowcontextpreservation.ui.theme.KotlinFlowContextPreservationTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KotlinFlowContextPreservationTheme {
            lifecycleScope.launch(Dispatchers.Main) {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                 try {
                     producer()
                         .collect {
                             Log.d("Producer", "Collecting thread - ${Thread.currentThread().name}")
                         }
                 }catch (e: Exception){
                     Log.d("Producer", e.message.toString())
                 }


            }
            }
            }
        }
    }
    private fun producer(): Flow<Int> {
        return flow<Int>{
            val list = listOf(1, 2, 3, 4, 5)
                list.forEach {
                    delay(1000)
                    Log.d("Producer", "Emitting  thread - ${Thread.currentThread().name}")
                    emit(it)
                    throw Exception("Error in Emitting")
                }
            }


    }
}

//.flowOn(Dispatchers.IO)
//.map {
//    delay(1000)
//    it*2
//    Log.d("Producer", "Mapping thread - ${Thread.currentThread().name}")
//}.filter {
//    delay(1000)
//    Log.d("Producer", "Filtering thread - ${Thread.currentThread().name}")
//    it % 2 == 0
//}


