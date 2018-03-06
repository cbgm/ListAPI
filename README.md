# ListAPI

An API for the easy usage of an **Android** configureable RecyclerView with the features of a **sort mechanism** and adding fast **custom buttons with slide function**.
It also supports multiple list items!

![](https://cdn.pbrd.co/images/1kdzBq37m.png)


## Usage:

1. Define your foreground layout and the item (ViewItem and Holder) itself

##### Just a normal XML layout

        <TextView android:id="@+id/txt_dynamic2" xmlns:android="http://schemas.android.com/apk/res/android"
        android:textAppearance="?android:attr/textAppearanceMedium"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:minWidth="60dp"
        android:maxWidth="300dp"
        ...
  -------------   
  
##### When defining the a new list item extend from CBAdapterDelegate { 

        public class AdapterDelegate1 extends CBAdapterDelegate<BaseItem> {
              .....

##### Define the view by creating a view holder inside extended from CBAdapterDelegate.CBViewHolder
                public class MyViewHolder extends CBAdapterDelegate.CBViewHolder {
                        private TextView name;

                        MyViewHolder(View itemView, ViewGroup parent, int itemRessource) {
                            super(itemView, parent, itemRessource, true, true);
                            this.addCustomButton(new MyButton(CustomLayoutID.CUSTOMBUTTON_ID, R.color.colorAccent, -1), parent.getContext());
                            this.name = itemView.findViewById(R.id.txt_dynamic2);
                        }
                }
##### Init the view by call in constructor

                @Override
                protected MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        return new MyViewHolder(CBBaseView.getView(parent.getContext()), parent, R.layout.backitem_standard);
                }

##### Setup view funtionality
               @Override
               protected void onBindDelegateViewHolder(final CBAdapterDelegate.CBViewHolder holder, final int position, final List<BaseItem> data) {
                        final FirstItem item = (FirstItem) data.get(position);
                        final MyViewHolder holderFinal = (MyViewHolder) holder;
                        holderFinal.name.setText(item.getTest());
                        holderFinal.name.setEnabled(true);
                        holderFinal.name.setTextColor(Color.GREEN);
                        holderFinal.getButtonContainer().findViewById(CustomLayoutID.CUSTOMBUTTON_ID).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (CBModeHelper.getInstance().isItemTouchCurrentItem(position))
                                    myMenuListener.test(item);
                            }
                        });
                        holderFinal.itemView.setTag(holderFinal);
                }

                @Override
                protected boolean isTypeOf(List<BaseItem> data, int position) {
                        return data.get(position) instanceof FirstItem;
                }
        
3. Define the Fragment which uses the list

                public class MyFragment extends Fragment implements ICBActionDelegate<BaseItem> {
                        private List<BaseItem> viewItems;
                        private CBListView<MyHolder, BaseItem, MyAdapter> listContainer;
                        private MyAdapter adapter;

                        @Override
                        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

                                View rootView = inflater.inflate(R.layout.fragment, container, false);
                                this.listContainer = find(rootView, R.id.list_container);
                                List<CBAdapterDelegate> delegates = new ArrayList<>();
                                delegates.add(new AdapterDelegate1(this));
                                delegates.add(new AdapterDelegate2());
                                loadData();
                                setHasOptionsMenu(true);
##### Call the list setup
                                this.listContainer.init(this.viewItems, this.adapter);
                                return rootView;
                        }
                        ......

###### When the foreground was clicked do at least something

            @Override
            public void delegateSingleClickAction(int position) {

            }

###### Called when sorting is done

            @Override
            public void delegateSortAction(List list) {

            }
        }     
###### Called when swipe is done

            @Override
            public void delegateSwipeAction() {

            }
        }     
4. Switch from swipe / select to sort list mode

         CBModeHelper.getInstance().setListMode(CBListMode.SORT);
         loadData();
        
5. 

## Example

For a complete Example checkout: [https://github.com/cbgm/ListAPI_Example](https://github.com/cbgm/ListAPI_Example)

## License

Licensed under the MIT license. (http://opensource.org/licenses/MIT)
