package at.kcode.guerri.data

import at.kcode.guerri.data.model.QuestionDTO
import at.kcode.guerri.data.model.QuestionsDTO
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.withContext
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuestionRepository @Inject constructor() {

    private val moshi by lazy {
        Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }
    private val adapter = moshi.adapter(QuestionsDTO::class.java)

    val question = Channel<List<QuestionDTO>>(Channel.CONFLATED)

    suspend fun loadQuestions(jsonInputStream: InputStream) {
        val questions = withContext(Dispatchers.IO) {
            kotlin.runCatching {
                jsonInputStream
                    .bufferedReader(Charsets.UTF_8)
                    .use { it.readText() }
                    .let { rawJson ->
                        adapter.fromJson(rawJson)?.questions ?: throw Exception("Could not parse questions")
                    }
            }
        }.getOrThrow()

        question.send(questions)
    }

}