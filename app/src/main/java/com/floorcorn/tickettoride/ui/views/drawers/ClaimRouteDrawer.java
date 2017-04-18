package com.floorcorn.tickettoride.ui.views.drawers;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.floorcorn.tickettoride.R;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.PlayerColor;
import com.floorcorn.tickettoride.model.Route;
import com.floorcorn.tickettoride.model.TrainCardColor;
import com.floorcorn.tickettoride.ui.presenters.IBoardMapPresenter;
import com.floorcorn.tickettoride.ui.views.activities.BoardmapActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Tyler on 3/22/2017.
 */

public class ClaimRouteDrawer extends BMDrawer {

    private TextView header;
    private RouteRecyclerViewAdapter routeAdapter;
    private RecyclerView routeRecyclerView;
    private SearchView routeSearchView;
    private BoardmapActivity parentActivity;
    private List<Route> allRoutes;
    private List<Route> displayedList;


    public ClaimRouteDrawer(AppCompatActivity activity, IBoardMapPresenter presenter) {
        super(activity, presenter);
        allRoutes = new ArrayList<>();
        assert activity instanceof BoardmapActivity;
        parentActivity = (BoardmapActivity) activity;

        BM_DRAWER_LAYOUT.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {

            @Override
            public void onDrawerOpened(View drawerView) {
                if (drawerView.findViewById(R.id.drawer_place_routes) != null) {
                    if(parentPresenter != null)
                        parentPresenter.openedRoutes();
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                if (drawerView.findViewById(R.id.drawer_place_routes) != null) {
                    if(parentPresenter != null)
                        parentPresenter.closedRoutes();
                }
            }
        });
    }

    public void setList(List<Route> routes) {
        allRoutes.clear();
        allRoutes.addAll(routes);
        if (isOpen())
            routeAdapter.swapList(displayedList);
    }

    @Override
    public void open() {
        LayoutInflater inflater = (LayoutInflater) parentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.drawer_place_routes, null);
        DRAWER_HOLDER.removeAllViews();
        DRAWER_HOLDER.addView(layout);
        BM_DRAWER_LAYOUT.openDrawer(GravityCompat.START);


        header = (TextView)parentActivity.findViewById(R.id.choose_route_header);
        PlayerColor pc = parentPresenter.getGame().getPlayer(parentPresenter.getUser().getUserID()).getColor();
        header.setBackground(parentActivity.getPlayerHeader(pc));

        routeRecyclerView = (RecyclerView) parentActivity.findViewById(R.id.route_recycler);
        assert routeRecyclerView != null;
        this.allRoutes.clear();
        allRoutes.addAll(parentPresenter.getRoutes());
        this.displayedList = allRoutes;
        setupRecyclerView(routeRecyclerView, allRoutes);

        routeSearchView = (SearchView) parentActivity.findViewById(R.id.route_search_view);
        routeSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                listAll();
                return false;
            }
        });
        routeSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()){
                    listAll();
                }
                filter(newText);
                return true;
            }
        });
    }

    private void listAll(){
        allRoutes.clear();
        allRoutes.addAll(parentPresenter.getRoutes());
        displayedList = allRoutes;
        routeAdapter.swapList(displayedList);
    }

    private void filter(String text){
        List<Route> temp = new ArrayList<>();
        for(Route d : allRoutes){
            if(d.getEnglish().contains(text.toLowerCase())){
                temp.add(d);
            }
        }
        //update recyclerview
        this.displayedList = temp;
        routeAdapter.swapList(this.displayedList);
    }

    @Override
    public void hide() {
        if (isOpen()) {
            BM_DRAWER_LAYOUT.closeDrawer(Gravity.START);
        }
    }

    @Override
    public boolean isOpen() {
        if (BM_DRAWER_LAYOUT.isDrawerOpen(GravityCompat.START)) {
            LinearLayout tempFrame = (LinearLayout) parentActivity.findViewById(R.id.drawer_place_routes);
            return tempFrame != null;
        }
        return false;
    }


    /**
     * This sets up the Recycler view with an adapter.
     *
     * @param recyclerView RecyclerView object
     * @param routes       List of Route objects
     * @pre recyclerView != null
     * @pre game board initialized so recycler view can be displayed
     * @post displays as many lines as fit on screen or until end of list from routes
     * @post recycler view is scrollable (showing other items from routes list)
     * @post recycler view reuses UI elements (might have persisting style effects, etc)
     */
    private void setupRecyclerView(@NonNull RecyclerView recyclerView, List<Route> routes) {
        recyclerView.setLayoutManager(new LinearLayoutManager(parentActivity));
        recyclerView.setAdapter(new RouteRecyclerViewAdapter(routes));
        assert recyclerView.getAdapter() != null;
        routeAdapter = (RouteRecyclerViewAdapter) recyclerView.getAdapter();
    }

    public void openWildRouteDialog(Route r) {
        final Route toClaim = r;
        final List<String> eligibleColors = new ArrayList<>();
        Player p = parentPresenter.getGame().getPlayer(parentPresenter.getUser().getUserID());
        Map<TrainCardColor, Integer> trainCards = p.getTrainCards();
        int num_wilds = 0;
        if (trainCards.containsKey(TrainCardColor.WILD)){
            num_wilds += trainCards.get(TrainCardColor.WILD);
        }
        for (TrainCardColor color: trainCards.keySet()){
            if (color == TrainCardColor.WILD){
                if (trainCards.get(color) >= r.getLength())
                    eligibleColors.add(color.toString());
            }
            else if (trainCards.get(color) > 0){
                if (trainCards.get(color) + num_wilds >= r.getLength())
                    eligibleColors.add(color.toString());
            }
        }
        CharSequence[] eligibleArray = new CharSequence[eligibleColors.size()];
        int i = 0;
        for (String c: eligibleColors) {
            eligibleArray[i] = c;
            i++;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
        builder.setTitle("Pick Color to Claim Wild Route")
                .setItems(eligibleArray, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        toClaim.setColor(TrainCardColor.convertString(eligibleColors.get(which)));
                        System.out.println("chosen Color: "+toClaim.getColor());
                        parentPresenter.claimButtonClicked(toClaim);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    /**
     * This is the Adapter for showing all the routes. It shows a grid, each row corresponding to a
     * route.
     *
     * @pre game board is initialized so that route recycler view can exist and be displayed
     * @post after creating this object, list of routes will be displayed (as many as fit on
     * screen in the recycler view)
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
         * @param routes
         * @pre game board initialized
         * @pre RecyclerView exists
         * @pre routes != null
         * @post this.routes set to new ArrayList<>(routes param)
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
         * @param list List of Route objects
         * @pre (none)
         * @post delete(old(this.routes))
         * @post addAll(new(routes) to this.routes)
         * @post adapter notifies observers that data set changed
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
             * @param itemView
             * @pre itemView != null
             * @pre game board is initialized
             * @post route information displayed in an holder entry within the recycler view. Info
             * includes city 1, city 2, color, length, and owner information, as well as a
             * button for claiming the route.
             */
            ViewHolder(View itemView) {
                super(itemView);
                itemLayout = (LinearLayout) itemView;
                city1 = (TextView) itemLayout.getRootView().findViewById(R.id.city1);
                city2 = (TextView) itemLayout.getRootView().findViewById(R.id.city2);
                routeColor = (TextView) itemLayout.getRootView().findViewById(R.id.color);
                routeLength = (TextView) itemLayout.getRootView().findViewById(R.id.length);
                claimButton = (Button) itemLayout.getRootView().findViewById(R.id.claimButton);
                owner = (TextView) itemLayout.getRootView().findViewById(R.id.routeOwner);
            }

            public String getEnglish(){
                return city1.getText()+" "+city2.getText();
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
                    if (r.getColor() == TrainCardColor.WILD && parentPresenter.openWildRouteDialog()){
                            openWildRouteDialog(r);
                    }
                    else{
                        parentPresenter.claimButtonClicked(r);
                    }

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
