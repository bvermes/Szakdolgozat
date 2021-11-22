package hu.bme.aut.pred2

import android.R.attr
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import hu.bme.aut.pred2.R.id.button
import hu.bme.aut.pred2.R.id.editTextNumberDecimal
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.io.IOException
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import android.R.attr.value

import android.content.Intent




//////
private val MODEL_ASSETS_PATH = "dfwinnerpredict1121.tflite"
private var tflite : Interpreter? = null
///////

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//https://github.com/shubham0204/Spam_Classification_Android_Demo/blob/master/app/src/main/java/com/ml/quaterion/spamo/Classifier.kt
//https://www.youtube.com/watch?v=RhjBDxpAOIc&ab_channel=TensorFlow
        tflite = Interpreter( loadModelFile() )
        var ed1 : EditText = findViewById(editTextNumberDecimal)
        var button : Button = findViewById<Button>(button)
        var teambutton : Button = findViewById<Button>(R.id.teams)

        teambutton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, TeamActivity::class.java)
            intent.putExtra("key", 21)
            startActivity(intent)
        })
        button.setOnClickListener(View.OnClickListener {
            var v1: Float = ed1.text.toString().toFloat()
            var inputs: Array<Float> = arrayOf(v1, 3F, 3F, 3F, 0F, 10F, 10F, 10F, 1.5F, 3F, 5F)
            var results = classifySequence(inputs)
            var class1 = results[0]
            var class2 = results[1]
            var class3 = results[2]
            var Hometv : TextView = findViewById(R.id.HometextView)
            var Drawtv : TextView = findViewById(R.id.DrawtextView)
            var Awaytv : TextView = findViewById((R.id.AwaytextView))
            Hometv.setText(class1.toString())
            Drawtv.setText(class2.toString())
            Awaytv.setText(class3.toString())
        })
//////
//
        //var byteBuffer : ByteBuffer = ByteBuffer.allocateDirect(11*4)
        ////byteBuffer.putInt(v1)
        //byteBuffer.putInt(15)
        //byteBuffer.putInt(15)
        //byteBuffer.putInt(15)
        //byteBuffer.putInt(15)
        //byteBuffer.putInt(1)
        //byteBuffer.putInt(0)
        //byteBuffer.putInt(0)
        //byteBuffer.putInt(0)
        //byteBuffer.putInt(2)
        //byteBuffer.putInt(3)
        //byteBuffer.putInt(4)
//
        //// Creates inputs for reference.
        //val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 11), DataType.FLOAT32)
        //inputFeature0.loadBuffer(byteBuffer)
//
        //val model = Dfwinnerpredict.newInstance(this)
        //// Runs model inference and gets result.
        //val outputs = model.process(inputFeature0)
        //val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray
//
        //var tv : TextView = findViewById(R.id.textView)
//
        //tv.setText(outputFeature0[0].toString())
//
        //// Releases model resources if no longer used.
        //model.close()
    }
    @Throws(IOException::class)
    private fun loadModelFile(): MappedByteBuffer {
        val assetFileDescriptor = assets.openFd(MODEL_ASSETS_PATH)
        val fileInputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
        val fileChannel = fileInputStream.channel
        val startOffset = assetFileDescriptor.startOffset
        val declaredLength = assetFileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    private fun classifySequence (sequence: Array<Float>): FloatArray {
        val inputs : Array<FloatArray> = arrayOf( sequence.map { it.toFloat() }.toFloatArray() )
        var outputs : Array<FloatArray> = arrayOf( FloatArray( 3 ) )
        tflite?.run( inputs , outputs )
        return outputs[0]
    }
}