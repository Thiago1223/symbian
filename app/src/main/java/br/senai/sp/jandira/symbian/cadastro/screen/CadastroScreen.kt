package br.senai.sp.jandira.symbian.cadastro.screen

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.PermIdentity
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleCoroutineScope
import br.senai.sp.jandira.symbian.HomeActivity
import br.senai.sp.jandira.symbian.MainActivity
import br.senai.sp.jandira.symbian.R
import br.senai.sp.jandira.symbian.cadastro.components.CustomOutlinedTextField
import br.senai.sp.jandira.symbian.cadastro.components.DefaultButton
import br.senai.sp.jandira.symbian.repository.UserRepository
import coil.compose.AsyncImage
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.launch

@Composable
fun CadastroScreen(
    lifecycleScope: LifecycleCoroutineScope
) {

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val storageRef: StorageReference = FirebaseStorage.getInstance().reference.child("images")

    val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
        uri?.let { imageUri = it }
    }

//    val storageRef: StorageReference = FirebaseStorage.getInstance().reference.child("images")
//
//    val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
//
//    var imageUri by remember {
//        mutableStateOf<Uri?>(null)
//    }
//
//    var selectedMedia by remember {
//        mutableStateOf<List<Uri>>(emptyList())
//    }
//
//    val launcher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()
//    ) { uri ->
//        uri?.let {
//            imageUri = it
//            selectedMedia = selectedMedia + listOf(it)
//        }
//    }

    var nameState by remember {
        mutableStateOf("")
    }

    var passwordState by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current


    var validateName by rememberSaveable {
        mutableStateOf(true)
    }

    var validatePassword by rememberSaveable {
        mutableStateOf(true)
    }

    var isPasswordVisible by rememberSaveable {
        mutableStateOf(false)
    }

    val validateNameError = "Nome não pode ficar em branco"
    val validatePasswordError =
        "Deve misturar letras maiúsculas e minúsculas, pelo menos um número, caracter especial e mínimo de 8 caracteres"

    fun validateData(
        name: String,
        password: String,
        imagem: String
    ): Boolean {
        val passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#\$%^&+=!]).{8,}\$".toRegex()

        validateName = name.isNotBlank()
        validatePassword = passwordRegex.matches(password)

        return validateName && validatePassword
    }

    fun register(
        login: String,
        senha: String,
        imagem: String
    ) {
        if (validateData(login, senha, imagem)) {
            val userRepository = UserRepository()
            lifecycleScope.launch {

                val response = userRepository.registerUser(login, senha, imagem)

                if (response.isSuccessful) {
                    Log.d(MainActivity::class.java.simpleName, "$response")
                    Log.d(MainActivity::class.java.simpleName, "Registro bem-sucedido")

                    var openSignUp = Intent(context, HomeActivity::class.java)

                    //start a Activity
                    context.startActivity(openSignUp)

                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e(
                        MainActivity::class.java.simpleName,
                        "Erro durante o registro: $errorBody"
                    )
                    Toast.makeText(context, "Erro durante o registro", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(context, "Por favor, reolhe suas caixas de texto", Toast.LENGTH_SHORT)
                .show()
        }

    }


    Surface(
        color = Color(248, 240, 236)
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(top = 40.dp, start = 15.dp, end = 15.dp, bottom = 40.dp)
                .fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.size(150.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Card(
                        modifier = Modifier
                            .size(150.dp)
                            .align(Alignment.Center),
                        shape = CircleShape,
                        border = BorderStroke(
                            width = 2.dp,
                            Brush.horizontalGradient(
                                listOf(
                                    colorResource(id = R.color.purple_200),
                                    Color.White
                                )
                            )
                        )
                    ) {
                        AsyncImage(
                            model = imageUri,
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Image(
                        painter = painterResource(
                            id = R.drawable.baseline_camera_alt_24
                        ),
                        contentDescription = "",
                        modifier = Modifier
                            .align(alignment = Alignment.BottomEnd)
                            .offset(x = 3.dp, y = 3.dp)
                            .size(40.dp)
                            .clickable {
                                launcher.launch("image/*")
                            },

                        )
                }

                Spacer(modifier = Modifier.height(80.dp))

                CustomOutlinedTextField(
                    value = nameState,
                    onValueChange = { nameState = it },
                    label = "Digite um nome de usuário",
                    showError = !validateName,
                    errorMessage = validateNameError,
                    leadingIconImageVector = Icons.Default.PermIdentity,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    borderColor = Color.Black
                )

                CustomOutlinedTextField(
                    value = passwordState,
                    onValueChange = { passwordState = it },
                    label = "Senha",
                    showError = !validatePassword,
                    errorMessage = validatePasswordError,
                    isPasswordField = true,
                    isPasswordVisible = isPasswordVisible,
                    onVisibilityChange = { isPasswordVisible = it },
                    leadingIconImageVector = Icons.Default.Password,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    borderColor = Color.Black
                )
            }
            DefaultButton(
                text = "Cadastrar",
                onClick = {

//                    if (selectedMedia.isNotEmpty()) {
//                        for (uri in selectedMedia) {
//                            val storageRef = storageRef.child("${uri.lastPathSegment}-${System.currentTimeMillis()}.jpg")
//                            storageRef.putFile(uri).addOnCompleteListener { task ->
//                                if (task.isSuccessful) {
//                                    storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
//                                        val map = hashMapOf("pic" to downloadUri.toString())
//                                        firebaseFirestore.collection("images").add(map).addOnCompleteListener { firestoreTask ->
//                                            if (firestoreTask.isSuccessful) {
//                                                Toast.makeText(context, "CERTO AO TENTAR REALIZAR O UPLOAD", Toast.LENGTH_SHORT).show()
//                                            } else {
//                                                Toast.makeText(context, "ERRO AO TENTAR REALIZAR O UPLOAD", Toast.LENGTH_SHORT).show()
//                                            }
//                                        }
//                                    }
//                                } else {
//                                    Toast.makeText(context, "ERRO AO TENTAR REALIZAR O UPLOAD", Toast.LENGTH_SHORT).show()
//                                }
//                            }
//                        }
//                    } else {
//                        Toast.makeText(context, "Selecione ao menos 1 imagem para prosseguir", Toast.LENGTH_SHORT).show()
//                    }

                    imageUri?.let {
                        val riversRef = storageRef.child("${it.lastPathSegment}-${System.currentTimeMillis()}.jpg")
                        val uploadTask = riversRef.putFile(it)
                        uploadTask.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                riversRef.downloadUrl.addOnSuccessListener { uri ->
                                    val map = HashMap<String, Any>()
                                    map["pic"] = uri.toString()
                                    firebaseFirestore.collection("images").add(map).addOnCompleteListener { firestoreTask ->
                                        if (firestoreTask.isSuccessful){
                                            Toast.makeText(context, "UPLOAD REALIZADO COM SUCESSO", Toast.LENGTH_SHORT).show()
                                            register(
                                                login = nameState,
                                                senha = passwordState,
                                                imagem = "$uri"
                                            )
                                        }else{
                                            Toast.makeText(context, "ERRO AO TENTAR REALIZAR O UPLOAD", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            } else {
                                Toast.makeText(context, "ERRO AO TENTAR REALIZAR O UPLOAD", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            )
        }
    }
}
