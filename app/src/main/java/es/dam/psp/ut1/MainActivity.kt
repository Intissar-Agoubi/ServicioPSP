package es.dam.psp.ut1

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import es.dam.psp.ut1.servicio.DescargaService
import es.dam.psp.ut1.ui.theme.ServicioPSPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ServicioPSPTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PantallaServicio(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun PantallaServicio(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val pid = Process.myPid()

    val pedirPermiso = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        Log.d(DescargaService.TAG, "Permiso de notificaciones concedido: $concedido")
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pedirPermiso.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("PID de la actividad: $pid")

        Spacer(Modifier.height(24.dp))

        Button(onClick = {
            val intent = Intent(context, DescargaService::class.java)
            ContextCompat.startForegroundService(context, intent)
        }) {
            Text("Iniciar")
        }

        Spacer(Modifier.height(12.dp))

        Button(onClick = {
            val intent = Intent(context, DescargaService::class.java)
            context.stopService(intent)
        }) {
            Text("Parar")
        }
    }
}