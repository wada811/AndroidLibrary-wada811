/*
 * Copyright 2013 wada811<at.wada811@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.wada811.app.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import at.wada811.android.library.R;

/**
 * An fragment that displays an expandable list of items by binding to a data
 * source implementing the ExpandableListAdapter, and exposes event handlers
 * when the user selects an item.
 */
public class ExpandableListFragment extends Fragment implements ExpandableListView.OnGroupCollapseListener, ExpandableListView.OnGroupExpandListener, ExpandableListView.OnGroupClickListener, ExpandableListView.OnChildClickListener {

    private static final int INTERNAL_EMPTY_ID              = R.id.INTERNAL_EMPTY_ID;
    private static final int INTERNAL_PROGRESS_CONTAINER_ID = R.id.INTERNAL_PROGRESS_CONTAINER_ID;
    private static final int INTERNAL_LIST_CONTAINER_ID     = R.id.INTERNAL_LIST_CONTAINER_ID;

    private final Handler    mHandler                       = new Handler();

    private final Runnable   mRequestFocus                  = new Runnable(){
                                                                @Override
                                                                public void run(){
                                                                    mList.focusableViewAvailable(mList);
                                                                }
                                                            };

    ExpandableListAdapter    mAdapter;
    ExpandableListView       mList;
    View                     mEmptyView;
    TextView                 mStandardEmptyView;
    View                     mProgressContainer;
    View                     mListContainer;
    CharSequence             mEmptyText;
    boolean                  mListShown;

    public ExpandableListFragment() {
    }

    /**
     * Provide default implementation to return a expandable list view. Subclasses
     * can override to replace with their own layout. If doing so, the returned
     * view hierarchy <em>must</em> have a ExpandableListView whose id
     * is {@link android.R.id#list android.R.id.list} and can optionally
     * have a sibling view id {@link android.R.id#empty android.R.id.empty} that is to be shown when
     * the list is empty.
     * 
     * <p>
     * If you are overriding this method with your own custom content, consider including the
     * standard layout {@link android.R.layout#list_content} in your layout file, so that you
     * continue to retain all of the standard behavior of ExpandableListFragment. In particular,
     * this is currently the only way to have the built-in indeterminant progress state be shown.
     * 
     * @param inflater The LayoutInflater object that can be used to inflate any views in the
     *        fragment,
     * @param container If non-null, this is the parent view that the fragment's UI should be
     *        attached to. The fragment should not add the view itself, but this can be used to
     *        generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
     *        saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState){
        final Context context = getActivity();

        final FrameLayout root = new FrameLayout(context);

        // ------------------------------------------------------------------

        final LinearLayout pframe = new LinearLayout(context);
        pframe.setId(INTERNAL_PROGRESS_CONTAINER_ID);
        pframe.setOrientation(LinearLayout.VERTICAL);
        pframe.setVisibility(View.GONE);
        pframe.setGravity(Gravity.CENTER);

        final ProgressBar progress = new ProgressBar(context, null, android.R.attr.progressBarStyleLarge);
        pframe.addView(progress, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        root.addView(pframe, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // ------------------------------------------------------------------

        final FrameLayout lframe = new FrameLayout(context);
        lframe.setId(INTERNAL_LIST_CONTAINER_ID);

        final TextView tv = new TextView(getActivity());
        tv.setId(INTERNAL_EMPTY_ID);
        tv.setGravity(Gravity.CENTER);
        lframe.addView(tv, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        final ExpandableListView lv = new ExpandableListView(getActivity());
        lv.setId(android.R.id.list);
        lv.setDrawSelectorOnTop(false);
        lframe.addView(lv, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        root.addView(lframe, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // ------------------------------------------------------------------

        root.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        return root;
    }

    /**
     * Attach to expandable list view once the view hierarchy has been created.
     * 
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
     *        saved state as given here.
     */
    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        ensureList();
    }

    /**
     * Detach from expandable list view.
     */
    @Override
    public void onDestroyView(){
        mHandler.removeCallbacks(mRequestFocus);
        mList = null;
        mListShown = false;
        mEmptyView = mProgressContainer = mListContainer = null;
        mStandardEmptyView = null;
        super.onDestroyView();
    }

    @Override
    public boolean onGroupClick(final ExpandableListView parent, final View v, final int groupPosition, final long id){
        return false;
    }

    /**
     * Override this for receiving callbacks when a group has been expanded.
     * 
     * @param groupPosition The group position that was expanded
     */
    @Override
    public void onGroupExpand(final int groupPosition){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO){
            mList.setSelectedGroup(groupPosition);
        }
    }

    /**
     * Override this for receiving callbacks when a group has been collapsed.
     * 
     * @param groupPosition The group position that was collapsed
     */
    @Override
    public void onGroupCollapse(final int groupPosition){
    }

    /**
     * Override this for receiving callbacks when a child has been clicked.
     * Callback method to be invoked when a child in this expandable list has been clicked.
     * 
     * @param parent The ExpandableListView where the click happened
     * @param v The view within the expandable list/ListView that was clicked
     * @param groupPosition The group position that contains the child that was clicked
     * @param childPosition The child position within the group
     * @param id The row id of the child that was clicked
     */
    @Override
    public boolean onChildClick(final ExpandableListView parent, final View v, final int groupPosition, final int childPosition, final long id){
        return false;
    }

    /**
     * Provide the adapter for the expandable list.
     */
    public void setListAdapter(final ExpandableListAdapter adapter){
        final boolean hadAdapter = mAdapter != null;
        mAdapter = adapter;
        if(mList != null){
            mList.setAdapter(adapter);
            if(!mListShown && !hadAdapter){
                // The list was hidden, and previously didn't have an
                // adapter.  It is now time to show it.
                setListShown(true, getView().getWindowToken() != null);
            }
        }
    }

    /**
     * Sets the selection to the specified group.
     * 
     * @param groupPosition The position of the group that should be selected.
     */
    public void setSelectedGroup(final int groupPosition){
        ensureList();
        mList.setSelectedGroup(groupPosition);
    }

    /**
     * Sets the selection to the specified child.
     * If the child is in a collapsed group, the group will only be expanded and child subsequently
     * selected if shouldExpandGroup is set to true, otherwise the method will return false.
     * 
     * @param groupPosition The position of the group that contains the child.
     * @param childPosition The position of the child within the group.
     * @param shouldExpandGroup Whether the child's group should be expanded if it is collapsed.
     * @return Whether the selection was successfully set on the child.
     */
    public boolean setSelectedChild(final int groupPosition, final int childPosition, final boolean shouldExpandGroup){
        ensureList();
        return mList.setSelectedChild(groupPosition, childPosition, shouldExpandGroup);
    }

    /**
     * Gets the position (in packed position representation) of the currently selected group or
     * child.
     * Use getPackedPositionType(long), getPackedPositionGroup(long), and
     * getPackedPositionChild(long) to unpack the returned packed position.
     * 
     * @return A packed position representation containing the currently selected group or child's
     *         position and type.
     */
    public long getSelectedPosition(){
        ensureList();
        return mList.getSelectedPosition();
    }

    /**
     * Gets the ID of the currently selected group or child.
     * 
     * @return The ID of the currently selected group or child.
     */
    public long getSelectedId(){
        ensureList();
        return mList.getSelectedId();
    }

    /**
     * Get the activity's expandable list view widget.
     */
    public ExpandableListView getExpandableListView(){
        ensureList();
        return mList;
    }

    /**
     * The default content for a ExpandableListFragment has a TextView that can be shown when the
     * list is empty.
     * If you would like to have it shown, call this method to supply the text it should use.
     */
    public void setEmptyText(final CharSequence text){
        ensureList();
        if(mStandardEmptyView == null){
            throw new IllegalStateException("Can't be used with a custom content view");
        }
        mStandardEmptyView.setText(text);
        if(mEmptyText == null){
            mList.setEmptyView(mStandardEmptyView);
        }
        mEmptyText = text;
    }

    /**
     * Control whether the list is being displayed.
     * You can make it not displayed if you are waiting for the initial data to show in it.
     * During this time an indeterminant progress indicator will be shown instead.
     * 
     * <p>
     * Applications do not normally need to use this themselves. The default behavior of
     * ListFragment is to start with the list not being shown, only showing it once an adapter is
     * given with {@link #setListAdapter(ListAdapter)}. If the list at that point had not been
     * shown, when it does get shown it will be do without the user ever seeing the hidden state.
     * 
     * @param shown If true, the list view is shown; if false, the progress indicator. The initial
     *        value is true.
     */
    public void setListShown(final boolean shown){
        setListShown(shown, true);
    }

    /**
     * Like {@link #setListShown(boolean)}, but no animation is used when transitioning from the
     * previous state.
     */
    public void setListShownNoAnimation(final boolean shown){
        setListShown(shown, false);
    }

    /**
     * Control whether the list is being displayed.
     * You can make it not displayed if you are waiting for the initial data to show in it.
     * During this time an indeterminant progress indicator will be shown instead.
     * 
     * @param shown If true, the list view is shown; if false, the progress indicator. The initial
     *        value is true.
     * @param animate If true, an animation will be used to transition to the new state.
     */
    private void setListShown(final boolean shown, final boolean animate){
        ensureList();
        if(mProgressContainer == null){
            throw new IllegalStateException("Can't be used with a custom content view");
        }
        if(mListShown == shown){
            return;
        }
        mListShown = shown;
        if(shown){
            if(animate){
                mProgressContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out));
                mListContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
            }else{
                mProgressContainer.clearAnimation();
                mListContainer.clearAnimation();
            }
            mProgressContainer.setVisibility(View.GONE);
            mListContainer.setVisibility(View.VISIBLE);
        }else{
            if(animate){
                mProgressContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
                mListContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out));
            }else{
                mProgressContainer.clearAnimation();
                mListContainer.clearAnimation();
            }
            mProgressContainer.setVisibility(View.VISIBLE);
            mListContainer.setVisibility(View.GONE);
        }
    }

    /**
     * Get the ExpandableListAdapter associated with this activity's ExpandableListView.
     */
    public ExpandableListAdapter getExpandableListAdapter(){
        return mAdapter;
    }

    private void ensureList(){
        if(mList != null){
            return;
        }
        final View root = getView();
        if(root == null){
            throw new IllegalStateException("Content view not yet created");
        }
        if(root instanceof ExpandableListView){
            mList = (ExpandableListView)root;
        }else{
            mStandardEmptyView = (TextView)root.findViewById(INTERNAL_EMPTY_ID);
            if(mStandardEmptyView == null){
                mEmptyView = root.findViewById(android.R.id.empty);
            }else{
                mStandardEmptyView.setVisibility(View.GONE);
            }
            mProgressContainer = root.findViewById(INTERNAL_PROGRESS_CONTAINER_ID);
            mListContainer = root.findViewById(INTERNAL_LIST_CONTAINER_ID);
            View rawListView = root.findViewById(android.R.id.list);
            if(!(rawListView instanceof ExpandableListView)){
                if(rawListView == null){
                    throw new RuntimeException("Your content must have a ExpandableListView whose id attribute is 'android.R.id.list'");
                }
                throw new RuntimeException("Content has view with id attribute 'android.R.id.list' that is not a ExpandableListView class");
            }
            mList = (ExpandableListView)rawListView;
            if(mEmptyView != null){
                mList.setEmptyView(mEmptyView);
            }else if(mEmptyText != null){
                mStandardEmptyView.setText(mEmptyText);
                mList.setEmptyView(mStandardEmptyView);
            }
        }
        mListShown = true;
//        mList.setOnCreateContextMenuListener(this);
        mList.setOnChildClickListener(this);
        mList.setOnGroupCollapseListener(this);
        mList.setOnGroupExpandListener(this);
        mList.setOnGroupClickListener(this);
        if(mAdapter != null){
            final ExpandableListAdapter adapter = mAdapter;
            mAdapter = null;
            setListAdapter(adapter);
        }else{
            // We are starting without an adapter, so assume we won't
            // have our data right away and start with the progress indicator.
            if(mProgressContainer != null){
                setListShown(false, false);
            }
        }
        mHandler.post(mRequestFocus);
    }

}
