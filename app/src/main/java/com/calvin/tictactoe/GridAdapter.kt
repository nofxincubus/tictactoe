package com.calvin.tictactoe

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.calvin.tictactoe.GridAdapter.GridViewHolder

class GridAdapter(private var mSquares: MutableList<Square>,
                  private val mListener: OnSquareClickListener?)
    : RecyclerView.Adapter<GridViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    var isFull = false

    init {
        isFull = false
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Int
            mListener?.onSquareClickListener(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item, parent, false)
        return GridViewHolder(view)
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        when (mSquares.get(position)) {
            Square.EMPTY -> holder.image.setImageDrawable(null)
            Square.X -> holder.image.setImageResource(R.drawable.outline_close_24px)
            Square.O -> holder.image.setImageResource(R.drawable.outline_radio_button_unchecked_24px)
        }

        with(holder.mView) {
            tag = position
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mSquares.size

    fun isClickable(position: Int): Boolean{
        when (mSquares.get(position)) {
            Square.EMPTY -> return true
            Square.X -> return false
            Square.O -> return false
        }
    }

    fun setSquare(position: Int, square: Square){
        mSquares[position] = square
        notifyDataSetChanged()
    }

    fun isGameOver(): Boolean {
        //Check horizontal
        for (i in 0 until 15 step 4) {
            if (mSquares[i] != Square.EMPTY) {
                val compare = mSquares[i] == mSquares[i+1] &&
                        mSquares[i] == mSquares[i+2] &&
                        mSquares[i] == mSquares[i+3]
                if (compare){
                    return compare
                }
            }
        }

        //Check vertical
        for (i in 0..3) {
            if (mSquares[i] != Square.EMPTY) {
                val compare = mSquares[i] == mSquares[i+4] &&
                        mSquares[i] == mSquares[i+8] &&
                        mSquares[i] == mSquares[i+12]
                if (compare){
                    return compare
                }
            }
        }

        //Check\
        if (mSquares[0] != Square.EMPTY) {
            val compare = mSquares[0] == mSquares[5] &&
                    mSquares[0] == mSquares[10] &&
                    mSquares[0] == mSquares[15]
            if (compare){
                return compare
            }
        }

        //Check/
        if (mSquares[3] != Square.EMPTY) {
            val compare = mSquares[3] == mSquares[6] &&
                    mSquares[3] == mSquares[9] &&
                    mSquares[3] == mSquares[12]
            if (compare){
                return compare
            }
        }

        //Full
        var full = true
        for (i in 0..15) {
            if (mSquares[i] == Square.EMPTY){
                full = false
                break; //we good
            }
        }

        isFull = full

        return full
    }

    inner class GridViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val image = itemView.findViewById<ImageView>(R.id.image)
    }
}