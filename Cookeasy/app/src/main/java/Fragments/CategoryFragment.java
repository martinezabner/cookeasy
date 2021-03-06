package Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import Adapters.CategoryAdapter;
import Adapters.RecipeAdapter;
import Common.OnFavTapListener;
import Common.OnItemTapListener;
import Data.RecipeRepository;
import Models.Recipe;
import uca.edu.ni.cookeasy.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryFragment extends Fragment implements OnFavTapListener, OnItemTapListener {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    private View view;
    private Context context = null;

    private String category;

    private TextView categoryName;
    private RecipeAdapter adapterRecipe;
    private RecyclerView.LayoutManager lmRecipes;
    private SearchView svCat;

    List<Recipe> recipeList = new ArrayList<>();

    RecipeRepository recipeRepository;

    RecyclerView rvRecipes;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    public CategoryFragment() {
        // Required empty public constructor
    }

    public CategoryFragment(Context context, String category) {
        this.context = context;
        this.category = category;
    }

    public static CategoryFragment newInstance(String param1, String param2) {
        CategoryFragment fragment = new CategoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_category, container, false);

        recipeRepository = new RecipeRepository(context);

        svCat = view.findViewById(R.id.sv_fav);
        svCat.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadData();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                loadData();
                return false;
            }
        });

        categoryName = view.findViewById(R.id.tv_category_name);
        categoryName.setText(category);

        rvRecipes = view.findViewById(R.id.rv_category);
        rvRecipes.setHasFixedSize(true);
        lmRecipes = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvRecipes.setLayoutManager(lmRecipes);
        adapterRecipe = new RecipeAdapter(recipeList, 3, this, this);
        rvRecipes.setAdapter(adapterRecipe);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        recipeRepository.fillData(recipes -> {
            recipeList = recipes;
            adapterRecipe.updateList(recipeList, svCat.getQuery(), category);
        });
    }

    @Override
    public void onFavTap(View view, int position) {
        addFavourite(view, position);
    }

    private Recipe getSelected(int position) {

        Recipe selectedRecipe;

        RecipeAdapter.RecipeViewHolder viewHolder =
                (RecipeAdapter.RecipeViewHolder)rvRecipes.findViewHolderForAdapterPosition(position);

        if(viewHolder == null) {
            Log.e("shit", "algo salio mal");
            return null;
        }

        for (int i = 0; i < recipeList.size(); i++) {
            if (recipeList.get(i).getName().equals(viewHolder.modelId)) {
                position = i;
            }
        }

        selectedRecipe = recipeList.get(position);

        return selectedRecipe;
    }

    public void addFavourite(View view, int position) {
        String message = "";
        Recipe selectedRecipe;
        int fav = 0;

        selectedRecipe = getSelected(position);

        if (selectedRecipe.getFavourite() == 0) {
            message = String.format("%s ha sido añadido a los favoritos", selectedRecipe.getName());
            fav = 1;
        } else if (selectedRecipe.getFavourite() == 1) {
            message = String.format("%s ha sido removido de los favoritos", selectedRecipe.getName());
            fav = 0;
        }

        updateDBFav(selectedRecipe.getId(), fav);

        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    private void updateDBFav(String id,  int fav) {
        reference = FirebaseDatabase.getInstance().getReference("recipees");
        reference.child(id).child("favourite").setValue(fav);
    }

    @Override
    public void onItemTapListener(View view, int position) {
        openRecipe(view, position);
    }

    private void openRecipe(View view, int position) {
        Recipe selectedRecipe;

        selectedRecipe = getSelected(position);

        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        RecipeFragment recipeFragment = new RecipeFragment(context, selectedRecipe);
        fragmentTransaction.replace(R.id.frg_main, recipeFragment);
        fragmentTransaction.commit();
        fragmentTransaction.addToBackStack(null);

    }
}