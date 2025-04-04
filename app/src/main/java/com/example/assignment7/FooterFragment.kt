import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.assignment7.R

class FooterFragment : Fragment() {

    private lateinit var totalAmountTextView: TextView
    private var totalAmount: Double = 0.0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        totalAmountTextView = view.findViewById(R.id.textViewTotalAmount)

        updateTotalAmount()
    }

    private fun updateTotalAmount() {
        totalAmountTextView.text = "Total Expense: $${"%.2f".format(totalAmount)}"
    }
}
