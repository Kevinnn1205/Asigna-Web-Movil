import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appmovilasignaweb.Espacio
import com.example.appmovilasignaweb.R
import com.example.appmovilasignaweb.Contenedor_crear_reserva

// Adaptador para mostrar los espacios en un RecyclerView
class EspacioAdapter(
    private val contexto: Context, // Añadimos contexto para manejar Intents
    private val espacioList: MutableList<Espacio>,
    private val onItemClick: (Espacio) -> Unit
) : RecyclerView.Adapter<EspacioAdapter.EspacioViewHolder>() {

    // Método que crea un nuevo ViewHolder cuando se necesita
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EspacioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_espacio, parent, false)
        return EspacioViewHolder(view)
    }

    // Método que une los datos de un objeto Espacio a un ViewHolder
    override fun onBindViewHolder(holder: EspacioViewHolder, position: Int) {
        val espacio = espacioList[position]

        // Asignar datos del objeto Espacio a los TextViews del ViewHolder
        holder.textViewNombreEspacio.text = espacio.nombre
        holder.textViewClasificacionEspacio.text = espacio.clasificacion
        holder.textViewCapacidadEspacio.text = espacio.capacidad
        holder.textViewDescripcionEspacio.text = espacio.descripcion

        // Configurar el listener para detectar clics en el item
        holder.itemView.setOnClickListener {
            onItemClick(espacio)

            // Navegar a Contenedor_crear_reserva y pasar los datos del espacio seleccionado
            val intent = Intent(contexto, Contenedor_crear_reserva::class.java).apply {
                putExtra("nombre", espacio.nombre)
                putExtra("clasificacion", espacio.clasificacion)
                putExtra("capacidad", espacio.capacidad)
                putExtra("descripcion", espacio.descripcion)
            }
            contexto.startActivity(intent)
        }
    }

    // Método que devuelve la cantidad de elementos en la lista
    override fun getItemCount(): Int {
        return espacioList.size
    }

    // ViewHolder que contiene las vistas del item (carta)
    class EspacioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewNombreEspacio: TextView = itemView.findViewById(R.id.textViewNombreEspacio)
        val textViewClasificacionEspacio: TextView = itemView.findViewById(R.id.textViewClasificacionEspacio)
        val textViewCapacidadEspacio: TextView = itemView.findViewById(R.id.textViewCapacidadEspacio)
        val textViewDescripcionEspacio: TextView = itemView.findViewById(R.id.textViewDescripcionEspacio)
    }
}
