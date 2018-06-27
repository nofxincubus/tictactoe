package com.calvin.tictactoe

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

interface OnSquareClickListener {
    fun onSquareClickListener(position: Int)
}

class MainActivity : AppCompatActivity(), OnSquareClickListener {

    private lateinit var gridLayoutManager: GridLayoutManager

    //O = false, X = true
    private var turn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gridLayoutManager = GridLayoutManager(this, 4)
        grid.layoutManager = gridLayoutManager

        startNewGame();

        newGame.setOnClickListener {
            startNewGame();
        }
    }

    fun startNewGame(){
        grid.adapter = GridAdapter(emptyGrid(), this)
        turn = false;
        updateTurn()
    }

    private fun emptyGrid() : MutableList<Square> {
        val mutableList = mutableListOf<Square>()
        for (i in 0..15) {
            mutableList.add(Square.EMPTY);
        }
        return mutableList
    }

    override fun onSquareClickListener(position: Int) {
        if ((grid.adapter as GridAdapter).isClickable(position)){
            (grid.adapter as GridAdapter).setSquare(position, if (turn) Square.X else Square.O )

            //Check game over
            if ((grid.adapter as GridAdapter).isGameOver()){
                if ((grid.adapter as GridAdapter).isFull){
                    showNewGameDialog(getString(R.string.draw))
                } else {
                    showNewGameDialog(getString(R.string.won, if (turn) "X" else "O"))
                }
            } else { // next turn
                if (turn) turn = false else turn = true
                updateTurn()
            }
        }
    }

    fun updateTurn(){
        currentPlayerIcon.setImageResource(
                if (turn)
                    R.drawable.outline_close_24px
                else
                    R.drawable.outline_radio_button_unchecked_24px
        )
    }

    fun showNewGameDialog(title: String){
        AlertDialog.Builder(this)
                .setTitle(title)
                .setPositiveButton(android.R.string.ok, { dialog, whichButton ->
                    startNewGame()
                }).create().show()
    }

}
