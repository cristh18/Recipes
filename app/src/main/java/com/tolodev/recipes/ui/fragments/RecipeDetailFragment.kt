package com.tolodev.recipes.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import androidx.core.text.color
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.tolodev.recipes.R
import com.tolodev.recipes.databinding.FragmentRecipeDetailBinding
import com.tolodev.recipes.ui.models.Recipe

class RecipeDetailFragment : Fragment() {

    private var binding: FragmentRecipeDetailBinding? = null

    private var recipe: Recipe? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val args = RecipeDetailFragmentArgs.fromBundle(it)
            recipe = args.recipeSelected
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        recipe?.let { recipeSelected ->
            binding?.run {
                textViewRecipeDetailTitle.text = recipeSelected.title
                textViewRecipeDetailDescription.text = getFormattedText(
                    getString(R.string.copy_description),
                    recipeSelected.description
                )
                Glide.with(root.context).load(recipeSelected.photoUrl)
                    .into(imageViewRecipeDetailImage)
                showTags(recipeSelected)
                showChefName(recipeSelected)
            }
        }
    }

    private fun initHorizontalScrollView(tags: List<String>) {
        tags.forEach {
            val chip = Chip(context).apply {
                layoutParams = ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                text = it
                chipIcon = ContextCompat.getDrawable(context, R.drawable.ic_tag)
                setIconStartPaddingResource(R.dimen.spacing_standard)
            }
            binding?.chipGroupRecipeTags?.addView(chip)
        }
    }

    private fun FragmentRecipeDetailBinding.showChefName(recipeSelected: Recipe) {
        val canShowChefName = recipeSelected.chefName.isNotBlank()
        textViewRecipeDetailChef.isVisible = canShowChefName
        if (canShowChefName) {
            textViewRecipeDetailChef.text =
                getFormattedText(getString(R.string.copy_chef), recipeSelected.chefName)
        }
    }

    private fun FragmentRecipeDetailBinding.showTags(recipeSelected: Recipe) {
        val canShowTags = recipeSelected.tags.isNotEmpty()
        hsvRecipeDetailTags.isVisible = canShowTags
        if (canShowTags) {
            initHorizontalScrollView(recipeSelected.tags)
        }
    }

    private fun getFormattedText(label: String, text: String): SpannableStringBuilder {
        return SpannableStringBuilder().apply {
            bold {
                color(
                    ContextCompat.getColor(
                        this@RecipeDetailFragment.requireContext(),
                        R.color.black
                    )
                ) {
                    appendLine(label)
                    appendLine()
                }
            }.append(text)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        requireActivity().menuInflater.inflate(R.menu.main_menu, menu);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        if (id == R.id.action_share) {
            shareRecipe()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun shareRecipe() {
        recipe?.let {
            val uriImage: Uri = Uri.parse(recipe?.photoUrl)
            val shareIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                val bundle: Bundle = Bundle().apply {
                    putString(Intent.EXTRA_TEXT, recipe?.title.orEmpty())
                    type = "text/plain"
                    putParcelable(Intent.EXTRA_STREAM, uriImage)
                    type = "image/jpeg"
                }
                putExtras(bundle)
            }
            startActivity(Intent.createChooser(shareIntent, getString(R.string.copy_send_to)))
        }
    }
}