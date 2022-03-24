package com.gregorchristiaens.introduction.lessons

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.gregorchristiaens.introduction.R
import com.gregorchristiaens.introduction.databinding.LessonCardBinding
import com.gregorchristiaens.introduction.domain.Lesson
import com.gregorchristiaens.introduction.repository.LessonRepository
import com.gregorchristiaens.introduction.repository.UserRepository

class LessonAdapter : RecyclerView.Adapter<LessonAdapter.ViewHolder>() {

    private val logKey = "IntroductionApp.LOGKEY.LessonAdapter"

    lateinit var binding: LessonCardBinding
    var lessons: List<Lesson> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = binding.title
        val date: TextView = binding.date
        val gear1: ImageView = binding.img1
        val gear2: ImageView = binding.img2
        val participants: TextView = binding.participants
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = LessonCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lesson = lessons[position]
        holder.title.text = lesson.name
        holder.date.text = lesson.dateString
        when (lesson.type.gear[0]) {
            "tonfa" -> {
                holder.gear1.visibility = View.VISIBLE
                holder.gear1.setImageResource(R.drawable.tonfa)
            }
            "bo" -> {
                holder.gear1.visibility = View.VISIBLE
                holder.gear1.setImageResource(R.drawable.bo)
            }
            "sai" -> {
                holder.gear1.visibility = View.VISIBLE
                holder.gear1.setImageResource(R.drawable.sai)
            }
            "nunchaku" -> {
                holder.gear1.visibility = View.VISIBLE
                holder.gear1.setImageResource(R.drawable.nunchaku)
            }
            "boxing gloves" -> {
                holder.gear1.visibility = View.VISIBLE
                holder.gear1.setImageResource(R.drawable.boxing_gloves)
            }
        }
        //TODO rethink this mess
        if (lesson.type.gear.size > 1) {
            when (lesson.type.gear[1]) {
                "shin guard" -> {
                    holder.gear2.visibility = View.VISIBLE
                }
            }
        }
        if (!lesson.users.isNullOrEmpty()) {
            holder.participants.visibility = View.VISIBLE
            holder.participants.text = lesson.users.size.toString()
            val userRepo = UserRepository.getInstance()
            val user = userRepo.user.value
            if (user != null) {
                if (lesson.users.contains(user.id)) {
                    holder.participants.setTextColor(Color.parseColor("#f4511e"))
                }
            }
        }
        binding.card.setOnClickListener {
            val lessonRepo = LessonRepository.getInstance()
            lessonRepo.setCurrentLesson(lesson)
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_lessonsFragment_to_lessonFragment)
        }
    }

    override fun getItemCount(): Int {
        return lessons.size
    }
}