import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.pelita.databinding.ItemDonasiBinding
import com.dicoding.pelita.models.DonationItem

class DonasiAdapter(private val donations: List<DonationItem>) :
    RecyclerView.Adapter<DonasiAdapter.DonationViewHolder>() {

    var onDonasiButtonClickListener: ((DonationItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonationViewHolder {
        val binding = ItemDonasiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DonationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DonationViewHolder, position: Int) {
        val donation = donations[position]
        holder.bind(donation)

        holder.binding.btndonasi.setOnClickListener {
            onDonasiButtonClickListener?.invoke(donation)
        }
    }

    override fun getItemCount(): Int = donations.size

    class DonationViewHolder(val binding: ItemDonasiBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(donation: DonationItem) {
            Glide.with(binding.root)
                .load(donation.imageUrl)
                .centerCrop()
                .into(binding.imgItem)

            binding.tvTitle.text = donation.organizer
            binding.tvDescription.text = donation.title
        }
    }
}

