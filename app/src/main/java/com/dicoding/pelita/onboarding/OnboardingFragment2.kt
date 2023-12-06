import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dicoding.pelita.R
import com.dicoding.pelita.databinding.FragmentOnboardingBinding

class OnboardingFragment2 : Fragment() {

    private lateinit var binding: FragmentOnboardingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageViewOnboarding.setImageResource(R.drawable.onboarding1)
        binding.textViewTitle.text = "Jelajahi Rekomendasi MPASI untuk Tumbuh Kembang Si Kecil"

    }
}
