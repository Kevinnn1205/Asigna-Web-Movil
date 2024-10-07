import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appmovilasignaweb.R

// Asegúrate de que tu clase Espacio tenga estas propiedades
data class Espacio(
    val nombre: String,
    val clasificacion: String,
    val capacidad: String,
    val descripcion: String,
    val imagenUrl: String // Asegúrate de que este campo sea correcto
)

class EspacioAdapter(private val espacioList: List<Espacio>, private val context: Context) : RecyclerView.Adapter<EspacioAdapter.EspacioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EspacioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_espacio, parent, false)
        return EspacioViewHolder(view)
    }

    override fun onBindViewHolder(holder: EspacioViewHolder, position: Int) {
        val espacio = espacioList[position]

        // Asegúrate de que las propiedades de espacio tengan valores
        holder.textViewNombreEspacio.text = espacio.nombre
        holder.textViewClasificacionEspacio.text = espacio.clasificacion
        holder.textViewCapacidadEspacio.text = espacio.capacidad
        holder.textViewDescripcionEspacio.text = espacio.descripcion
    }

    override fun getItemCount(): Int {
        return espacioList.size
    }

    class EspacioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewNombreEspacio: TextView = itemView.findViewById(R.id.textViewNombreEspacio)
        val textViewClasificacionEspacio: TextView = itemView.findViewById(R.id.textViewClasificacionEspacio)
        val textViewCapacidadEspacio: TextView = itemView.findViewById(R.id.textViewCapacidadEspacio)
        val textViewDescripcionEspacio: TextView = itemView.findViewById(R.id.textViewDescripcionEspacio)
    }
}
