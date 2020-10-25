package uz.suhrob.todoapp.ui.onboarding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.suhrob.todoapp.R
import uz.suhrob.todoapp.databinding.OnboardingItemBinding

class OnboardingAdapter : RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>() {
    private val images = arrayOf(R.drawable.events, R.drawable.superhero, R.drawable.analysis)
    private val titles = arrayOf("Welcome to Todo App", "Work happens", "Tasks and assign")
    private val contents = arrayOf(
        "Whats going to happen tomorrow?",
        "Get notified when work happens.",
        "Task and assign them to colleagues."
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder =
        OnboardingViewHolder(
            OnboardingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        holder.binding.onboardingItemImage.setImageResource(images[position])
        holder.binding.onboardingItemTitle.text = titles[position]
        holder.binding.onboardingItemContent.text = contents[position]
    }

    override fun getItemCount(): Int = images.size

    inner class OnboardingViewHolder(val binding: OnboardingItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}