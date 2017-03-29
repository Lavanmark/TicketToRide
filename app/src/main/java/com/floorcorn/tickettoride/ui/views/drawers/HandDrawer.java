package com.floorcorn.tickettoride.ui.views.drawers;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.floorcorn.tickettoride.R;
import com.floorcorn.tickettoride.communication.GameChatLog;
import com.floorcorn.tickettoride.communication.Message;
import com.floorcorn.tickettoride.model.DestinationCard;
import com.floorcorn.tickettoride.model.TrainCardColor;
import com.floorcorn.tickettoride.ui.presenters.BoardmapPresenter;
import com.floorcorn.tickettoride.ui.presenters.IBoardMapPresenter;

import java.util.List;
import java.util.Map;

/**
 * Created by Tyler on 3/21/2017.
 */

public class HandDrawer extends BMDrawer{
	/**
	 * TextView showing number of COLOR cards in the player's hand.
	 */
	private TextView redCount;
	private TextView orangeCount;
	private TextView yellowCount;
	private TextView greenCount;
	private TextView blueCount;
	private TextView purpleCount;
	private TextView blackCount;
	private TextView whiteCount;
	private TextView wildCount;
	/**
	 * TextView showing the train count for the player.
	 */
	private TextView trainCount;
	/**
	 * LinearLayout showing the destination tickets.
	 */
	private LinearLayout destinationTicketHolder;
	/**
	 * LinearLayout that holds chat stuff.
	 */
	private LinearLayout chatLayout;
	/**
	 * Button for sending chat messages.
	 */
	private Button sendMessageBut;
	/**
	 * EditText text field for composing chat messages.
	 */
	private EditText chatTextField;
	/**
	 * ScrollView holding the chat messages
	 */
	private ScrollView chatScroll;
	
	public HandDrawer(AppCompatActivity activity, final IBoardMapPresenter presenter) {
		super(activity, presenter);
		
		redCount = (TextView)activity.findViewById(R.id.red_card_count);
		orangeCount = (TextView)activity.findViewById(R.id.orange_card_count);
		yellowCount = (TextView)activity.findViewById(R.id.yellow_card_count);
		greenCount = (TextView)activity.findViewById(R.id.green_card_count);
		blueCount = (TextView)activity.findViewById(R.id.blue_card_count);
		purpleCount = (TextView)activity.findViewById(R.id.purple_card_count);
		blackCount = (TextView)activity.findViewById(R.id.black_card_count);
		whiteCount = (TextView)activity.findViewById(R.id.white_card_count);
		wildCount = (TextView)activity.findViewById(R.id.wild_card_count);
		
		trainCount = (TextView)activity.findViewById(R.id.train_count);
		
		destinationTicketHolder = (LinearLayout)activity.findViewById(R.id.destinationHolder);
		
		// Set up chat stuff.
		
		chatScroll = (ScrollView)activity.findViewById(R.id.chatScroll);
		scrollToBottom();
		chatLayout = (LinearLayout)activity.findViewById(R.id.chatHolder);
		chatTextField = (EditText)activity.findViewById(R.id.chatMessageField);
		sendMessageBut = (Button)activity.findViewById(R.id.sendMessageButton);
		sendMessageBut.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(chatTextField.getText().toString().isEmpty())
					return;
				if(!presenter.gameInProgress())
					return;
				presenter.sendMessage(chatTextField.getText().toString());
				chatTextField.setText("");
			}
		});
	}
	public void open() {
		BM_DRAWER_LAYOUT.openDrawer(GravityCompat.END);
		sendMessageBut.setEnabled(true);
		displayDestinationCards(parentPresenter.getDestinationCards());
	}
	
	public void hide() {
		BM_DRAWER_LAYOUT.closeDrawer(GravityCompat.END);
		sendMessageBut.setEnabled(false);
	}
	
	public void displayChatLog(GameChatLog log) {
		chatLayout.removeAllViews();
		for(Message message : log.getRecentMessages()) {
			TextView tv = new TextView(chatLayout.getContext());
			tv.setText(message.toString());
			chatLayout.addView(tv);
		}
		scrollToBottom();
	}
	
	public void displayTrainCards(Map<TrainCardColor, Integer> cards) {
		redCount.setText(String.valueOf(cards.containsKey(TrainCardColor.RED)? cards.get(TrainCardColor.RED) : 0));
		orangeCount.setText(String.valueOf(cards.containsKey(TrainCardColor.ORANGE)? cards.get(TrainCardColor.ORANGE) : 0));
		yellowCount.setText(String.valueOf(cards.containsKey(TrainCardColor.YELLOW)? cards.get(TrainCardColor.YELLOW) : 0));
		greenCount.setText(String.valueOf(cards.containsKey(TrainCardColor.GREEN)? cards.get(TrainCardColor.GREEN) : 0));
		blueCount.setText(String.valueOf(cards.containsKey(TrainCardColor.BLUE)? cards.get(TrainCardColor.BLUE) : 0));
		purpleCount.setText(String.valueOf(cards.containsKey(TrainCardColor.PURPLE)? cards.get(TrainCardColor.PURPLE) : 0));
		blackCount.setText(String.valueOf(cards.containsKey(TrainCardColor.BLACK)? cards.get(TrainCardColor.BLACK) : 0));
		whiteCount.setText(String.valueOf(cards.containsKey(TrainCardColor.WHITE)? cards.get(TrainCardColor.WHITE) : 0));
		wildCount.setText(String.valueOf(cards.containsKey(TrainCardColor.WILD)? cards.get(TrainCardColor.WILD) : 0));
	}
	
	public void displayTrainCarsLeft(int cars) {
		trainCount.setText(String.valueOf(cars));
	}
	
	public void displayDestinationCards(List<DestinationCard> destinationCardList) {
		destinationTicketHolder.removeAllViews();
		for(DestinationCard destinationCard : destinationCardList) {
			System.out.println("card complete: "+ destinationCard.isComplete());
			TextView textView = new TextView(destinationTicketHolder.getContext());
			String s = destinationCard.toString();
			textView.setText(s);
			destinationTicketHolder.addView(textView);
		}
	}
	
	public boolean isOpen() {
		return BM_DRAWER_LAYOUT.isDrawerOpen(GravityCompat.END);
	}
	
	private void scrollToBottom() {
		chatScroll.post(new Runnable() {
			@Override
			public void run() {
				chatScroll.fullScroll(ScrollView.FOCUS_DOWN);
			}
		});
	}
}
