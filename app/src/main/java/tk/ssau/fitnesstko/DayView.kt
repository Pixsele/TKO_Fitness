package tk.ssau.fitnesstko

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class DayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {

    enum class WorkoutState {
        NONE,
        PLANNED,    // Серная рамка
        TODAY,       // Зеленая рамка
        COMPLETED    // Черная рамка
    }

    var state: WorkoutState = WorkoutState.NONE
        set(value) {
            field = value
            updateAppearance()
        }

    init {
        setTextColor(Color.BLACK)
        textSize = 16f
        gravity = android.view.Gravity.CENTER
        setPadding(32, 32, 32, 32)
    }

    private fun updateAppearance() {
        background = when (state) {
            WorkoutState.PLANNED -> createBorderDrawable(Color.GRAY)
            WorkoutState.TODAY -> createBorderDrawable(Color.GREEN)
            WorkoutState.COMPLETED -> createBorderDrawable(Color.BLACK)
            else -> null
        }
    }

    private fun createBorderDrawable(color: Int): GradientDrawable {
        return GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setStroke(4.dpToPx(), color)
            setColor(Color.TRANSPARENT)
        }
    }

    private fun Int.dpToPx(): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }
}