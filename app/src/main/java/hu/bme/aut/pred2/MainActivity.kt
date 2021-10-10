package hu.bme.aut.pred2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import hu.bme.aut.pred2.ml.Homeodds
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var button : Button = findViewById<Button>(R.id.button)
        button.setOnClickListener(View.OnClickListener {
            var ed1 : EditText = findViewById(R.id.editTextNumberDecimal)
            var v1 : Float = ed1.text.toString().toFloat()

            var byteBuffer : ByteBuffer = ByteBuffer.allocateDirect(1*4)
            byteBuffer.putFloat(v1)

            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 1), DataType.FLOAT32)
            inputFeature0.loadBuffer(byteBuffer)

            val model = Homeodds.newInstance(this)

            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray

            var tv : TextView = findViewById(R.id.textView)

            tv.setText(outputFeature0[0].toString())
            model.close()

        })
    }
}