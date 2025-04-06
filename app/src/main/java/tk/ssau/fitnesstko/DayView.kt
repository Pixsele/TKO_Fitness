package tk.ssau.fitnesstko

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class DayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {

    var state: WorkoutState = WorkoutState.NONE
        set(value) {
            field = value
            updateState()
        }

    private fun updateState() {
        when (state) {
            WorkoutState.PLANNED -> {
                background = createDashedCircle()
                setTextColor(Color.BLACK)
            }

            WorkoutState.TODAY -> {
                background = createSolidCircle(Color.GREEN)
                setTextColor(Color.WHITE)
            }

            WorkoutState.COMPLETED -> {
                background = createSolidCircle(Color.GRAY)
                setTextColor(Color.WHITE)
            }

            else -> background = null
        }
    }

    private fun createDashedCircle(): Drawable {
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.OVAL
        shape.setStroke(2, Color.BLACK, 5f, 10f)
        return shape
    }

    private fun createSolidCircle(color: Int): Drawable {
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.OVAL
        shape.setColor(color)
        return shape
    }

    enum class WorkoutState { NONE, PLANNED, TODAY, COMPLETED }
}