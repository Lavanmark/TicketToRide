package com.floorcorn.tickettoride.ui.views.drawers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.floorcorn.tickettoride.R;
import com.floorcorn.tickettoride.model.PlayerColor;
import com.floorcorn.tickettoride.model.Route;
import com.floorcorn.tickettoride.ui.presenters.BoardmapPresenter;
import com.floorcorn.tickettoride.ui.views.activities.BoardmapActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tyler on 3/22/2017.
 */

public class ClaimRouteDrawer extends BMDrawer {
	
	private RouteRecyclerViewAdapter routeAdapter;
	private RecyclerView routeRecyclerView;
	
	private BoardmapActivity parentActivity;
	
	public ClaimRouteDrawer(AppCompatActivity activity, final BoardmapPresenter presenter) {
		super(activity, presenter);
		assert activity instanceof BoardmapActivity;
		parentActivity = (BoardmapActivity) activity;
	}
	
	public void setList(List<Route> routes) {
		if(isOpen())
			routeAdapter.swapList(routes);
	}
	
	@Override
	public void open() {
		LayoutInflater inflater = (LayoutInflater)parentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.drawer_place_routes, null);
		DRAWER_HOLDER.removeAllViews();
		DRAWER_HOLDER.addView(layout);
		BM_DRAWER_LAYOUT.openDrawer(GravityCompat.START);
		
		routeRecyclerView = (RecyclerView) parentActivity.findViewById(R.id.route_recycler);
		assert routeRecyclerView != null;
		List<Route> routes = parentPresenter.getRoutes();
		setupRecyclerView(routeRecyclerView, routes);
	}
	
	@Override
	public void hide() {
		if (isOpen()) {
			BM_DRAWER_LAYOUT.closeDrawer(Gravity.START);
		}
	}
	
	@Override
	public boolean isOpen() {
		if(BM_DRAWER_LAYOUT.isDrawerOpen(GravityCompat.START)) {
			LinearLayout tempFrame = (LinearLayout) parentActivity.findViewById(R.id.drawer_place_routes);
			return tempFrame != null;
		}
		return false;
	}
	
	
	/**
	 * This sets up the Recycler view with an adapter.
	 *
	 * @pre recyclerView != null
	 * @pre game board initialized so recycler view can be displayed
	 * @post displays as many lines as fit on screen or until end of list from routes
	 * @post recycler view is scrollable (showing other items from routes list)
	 * @post recycler view reuses UI elements (might have persisting style effects, etc)
	 * @param recyclerView RecyclerView object
	 * @param routes List of Route objects
	 */
	private void setupRecyclerView(@NonNull RecyclerView recyclerView, List<Route> routes) {
		recyclerView.setLayoutManager(new LinearLayoutManager(parentActivity));
		recyclerView.setAdapter(new RouteRecyclerViewAdapter(routes));
		assert recyclerView.getAdapter() != null;
		routeAdapter = (RouteRecyclerViewAdapter) recyclerView.getAdapter();
	}
	
	
	/**
	 * This is the Adapter for showing all the routes. It shows a grid, each row corresponding to a
	 * route.
	 *
	 * @pre game board is initialized so that route recycler view can exist and be displayed
	 * @post after creating this object, list of routes will be displayed (as many as fit on
	 * 		screen in the recycler view)
	 */
	class RouteRecyclerViewAdapter
			extends RecyclerView.Adapter<RouteRecyclerViewAdapter.ViewHolder> {
		
		/**
		 * List of Route objects corresponding the the routes on the board map for this game.
		 */
		List<Route> routes;
		
		/**
		 * Constructor for this RecyclerViewAdapter.
		 *
		 * @pre game board initialized
		 * @pre RecyclerView exists
		 * @pre routes != null
		 * @post this.routes set to new ArrayList<>(routes param)
		 * @param routes
		 */
		RouteRecyclerViewAdapter(List<Route> routes) {
			this.routes = new ArrayList<>(routes);
		}
		
		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View view = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.claim_list_content, parent, false);
			ViewHolder viewHolder = new ViewHolder(view);
			return viewHolder;
		}
		
		/**
		 * Swaps list of routes in this object, replacing it with parameter.
		 *
		 * @pre (none)
		 * @post delete(old(this.routes))
		 * @post addAll(new(routes) to this.routes)
		 * @post adapter notifies observers that data set changed
		 * @param list List of Route objects
		 */
		void swapList(List<Route> list) {
			this.routes.clear();
			this.routes.addAll(list);
			notifyDataSetChanged();
		}
		
		/**
		 * Inner class that extends RecyclerView ViewHolder for instances of this Recycler View.
		 */
		class ViewHolder extends RecyclerView.ViewHolder {
			LinearLayout itemLayout;
			TextView city1;
			TextView city2;
			TextView routeColor;
			TextView routeLength;
			Button claimButton;
			TextView owner;
			
			/**
			 * Constructor for this ViewHolder class.
			 *
			 * @pre itemView != null
			 * @pre game board is initialized
			 * @post route information displayed in an holder entry within the recycler view. Info
			 * 		includes city 1, city 2, color, length, and owner information, as well as a
			 * 		button for claiming the route.
			 * @param itemView
			 */
			ViewHolder(View itemView) {
				super(itemView);
				itemLayout = (LinearLayout) itemView;
				city1 = (TextView)itemLayout.getRootView().findViewById(R.id.city1);
				city2 = (TextView)itemLayout.getRootView().findViewById(R.id.city2);
				routeColor = (TextView)itemLayout.getRootView().findViewById(R.id.color);
				routeLength = (TextView)itemLayout.getRootView().findViewById(R.id.length);
				claimButton = (Button)itemLayout.getRootView().findViewById(R.id.claimButton);
				owner = (TextView)itemLayout.getRootView().findViewById(R.id.routeOwner);
			}
		}
		
		@Override
		public void onBindViewHolder(final ViewHolder holder, int position) {
			final Route r = routes.get(position);
			holder.city1.setText(r.getFirstCity().getName());
			holder.city2.setText(r.getSecondCity().getName());
			holder.routeColor.setText(r.getColor().toString());
			holder.routeLength.setText(String.valueOf(r.getLength()));
			if (parentPresenter.canClaim(r))
				holder.claimButton.setEnabled(true);
			else
				holder.claimButton.setEnabled(false);
			holder.claimButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					parentPresenter.claimButtonClicked(r);
				}
			});
			
			// Set some information (name and color) for the person who owns the route.
			holder.owner.setTextColor(0xff000000);
			holder.owner.setText(parentPresenter.getPlayerName(r.getOwner()));
			// This will reset the background color to white (important because a RecyclerView
			// recycles its elements) if the route is unclaimed, or it will set the background color
			// to match the player that owns it.
			PlayerColor pc = parentPresenter.getPlayerColor(r.getOwner());
			//TODO figure out a good way to access this.
			holder.itemView.setBackgroundColor(parentActivity.getPlayerColor(pc));
		}
		
		@Override
		public int getItemCount() {
			return this.routes.size();
		}
	}
}
