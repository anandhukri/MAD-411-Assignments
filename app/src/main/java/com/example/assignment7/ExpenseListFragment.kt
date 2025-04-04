import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment7.R

class ExpenseListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExpenseAdapter
    private lateinit var expenses: List<Expense>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_expense_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewExpenses)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        expenses = getExpenses()
        adapter = ExpenseAdapter(expenses)
        recyclerView.adapter = adapter
    }

    private fun getExpenses(): List<Expense> {
        return listOf(
            Expense("Lunch", 20.0, "2023-04-03", Currency.CAD, 20.0),
            Expense("Dinner", 30.0, "2023-04-03", Currency.EUR, 30.0)
        )
    }
}

