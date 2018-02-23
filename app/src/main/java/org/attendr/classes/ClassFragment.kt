package org.attendr.classes

import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.classes_card.*
import kotlinx.android.synthetic.main.fragment_classes.*
import org.attendr.R
import org.attendr.classes.models.ClassDetails
import java.util.ArrayList

/**
 * @author Pauldg7@gmail.com (Paul Gillis)
 */
class ClassFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater.inflate(R.layout.fragment_classes, container, false)

        val cardAdapter = CardAdapter()
        recyclerView.adapter = cardAdapter
        val manager = LinearLayoutManager(context)
        recyclerView.layoutManager = manager
        val details = ClassDetails()
        details.professor = "Silvia Bompadre"
        details.classTitle = "University Physics 1"
        details.percentage = 55
        cardAdapter.addClass(details)
        val details2 = ClassDetails()
        details2.professor = "Joe Guilliams"
        details2.classTitle = "CS 2050"
        details2.percentage = 96
        cardAdapter.addClass(details2)

        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView)

        return rootView
    }

    private val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

        private var background: Drawable? = null
        private var trashDrawable: Drawable? = null
        private var initiated: Boolean = false

        private fun init() {
            context?.let {
                background = ColorDrawable(ContextCompat.getColor(it, R.color.colorDeleteRed))
                trashDrawable = ContextCompat.getDrawable(it, R.drawable.trashcan)
                initiated = true
            }
        }

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

        }

        override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, originDX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
            val itemView = viewHolder.itemView
            if (viewHolder.adapterPosition == -1) {
                return
            }

            if (!initiated) {
                init()
            }

            var dX = originDX.toInt()

            val itemHeight = itemView.bottom - itemView.top

            if (-dX >= itemHeight) {
                dX = -itemHeight
            } else if (dX > 0) {
                dX = 0
            }

            val backgroundLeft = itemView.right + dX
            val backgroundRight = itemView.right

            background?.setBounds(backgroundLeft, itemView.top, backgroundRight, itemView.bottom)
            background?.draw(c)

            val intrinsicWidth = trashDrawable!!.intrinsicWidth
            val intrinsicHeight = trashDrawable!!.intrinsicWidth

            val canLeft = itemView.right - itemHeight / 2 - intrinsicWidth / 2
            val canRight = itemView.right - itemHeight / 2 + intrinsicWidth / 2
            val canTop = itemView.top + (itemHeight - intrinsicHeight) / 2
            val canBottom = canTop + intrinsicHeight
            trashDrawable?.setBounds(canLeft, canTop, canRight, canBottom)

            trashDrawable?.draw(c)

            super.onChildDraw(c, recyclerView, viewHolder, dX.toFloat(), dY, actionState, isCurrentlyActive)
        }
    }

    internal inner class CardAdapter : RecyclerView.Adapter<CardHolder>() {

        var classes: MutableList<ClassDetails> = ArrayList()

        fun addClass(details: ClassDetails) {
            classes.add(details)
            notifyItemInserted(classes.size - 1)
        }

        fun removeClass(details: ClassDetails) {
            val position = classes.indexOf(details)
            if (position != -1) {
                classes.remove(details)
                notifyItemRemoved(position)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
            return CardHolder(LayoutInflater.from(parent.context).inflate(R.layout.classes_card, parent, false))
        }

        override fun onBindViewHolder(holder: CardHolder, position: Int) {
            if (!classes.isEmpty() && classes.size > position) {
                holder.setData(classes[position])
            }
        }

        override fun getItemCount(): Int {
            return classes.size
        }
    }

    internal inner class CardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setData(details: ClassDetails) {
            percentageWheel.progress = details.percentage
            classTitle.text = details.classTitle
        }
    }
}