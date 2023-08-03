package com.example.consultasqx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.consultasqx.Util.ConfiguraBD;
import com.example.consultasqx.dao.DAOUsuario;
import com.example.consultasqx.model.Usuario;
import com.example.consultasqx.view.Home;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CadastroActivity extends AppCompatActivity {

    Usuario usuario;
    FirebaseAuth autenticacao;
    EditText campoNome, campoPhone, campoCpf, campoEmail, campoSenha, campoConfSenha,
            campoCrm, campoNomeClinica, campoTelefoneClinica, campoCoordenadaX, campoCoordenadaY;

    MaterialCardView selectCard, campoConvenio;
    DAOUsuario dao;

    FirebaseFirestore db;

    String nome, email, phone, cpf, senha, confSenha, crm, nomeDaClinica, teleDaClinica, coordX, coordY;

    String[] papel = {"Médico", "Paciente"};
    String escolha = "";
    TextView especialidade, convenio, localDaClinica, exemploLocal, representaY, representaX;
    TextInputLayout textInputLayoutPapel;
    boolean[] selectedEsps, selectedConvs;
    ArrayList<Integer> listaEspecialidades = new ArrayList<>();
    ArrayList<String> listaEsps = new ArrayList<>();
    ArrayList<Integer> listaConvenios = new ArrayList<>();
    ArrayList<String> listaConvs = new ArrayList<>();
    String[] especialidades = {"Cardiologista","Cirurgião","Clínico Geral","Dermatologista","Endocrinologista","Epidemiologista",
            "Gastroenterologista","Geneticista","Geriatra","Ginecologista","Infectologista","Nefrologista","Neurologista",
            "Oftalmologista","Oncologista","Ortopedista","Otorrinolaringologista","Pediatra","Pneumologista","Psiquiatra",
            "Radiologista","Reumatologista","Urologista"};

    String[] convenios = {"Banco do Brasil","Caixa Econômica Federal","Banco Bradesco","Banco Itaú","Banco Santander Brasil","Banco Safra",
            "Banco BTG Pactual","Banco Original","Banco Inter","Nubank","C6 Bank","Banco PAN","Banco Votorantim",
            "Banco Mercantil do Brasil","Banco Pine","Banco do Nordeste","Banco da Amazônia","Banco Banrisul","Banco do Estado do Rio Grande do Sul","Banco Daycoval",
            "Banco CCB Brasil","Banco Neon","Banco Rendimento", "Banco Modal", "Banco Fibra", "Banco Safra", "Banco Máxima"};

    AutoCompleteTextView autoCompleteTextViewPapel;
    ArrayAdapter<String> adapterItemsPapel;

    AutoCompleteTextView autoCompleteTextViewEspecialidade;
    ArrayAdapter<String> adapterItemsEspecialidade;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        dao = new DAOUsuario();

        db = FirebaseFirestore.getInstance();

        campoNome = findViewById(R.id.editTextNome);
        campoPhone = findViewById(R.id.editTextPhonePaciente);
        campoCpf = findViewById(R.id.editTextCpf);
        campoEmail = findViewById(R.id.editTextE_mail);
        campoSenha = findViewById(R.id.editTextSenha);
        campoConfSenha = findViewById(R.id.editTextConfirmarSenha);

        //especs = findViewById(R.id.selectCard);
        selectCard = findViewById(R.id.selectCard);
        campoConvenio = findViewById(R.id.convenio);
        campoCrm = findViewById(R.id.editCrm);
        campoNomeClinica = findViewById(R.id.editNomeDaClinica);
        campoTelefoneClinica = findViewById(R.id.editTelefoneDaClinica);
        campoCoordenadaX = findViewById(R.id.editCoordenadaX);
        campoCoordenadaY = findViewById(R.id.editCoordenadaY);

        textInputLayoutPapel = findViewById(R.id.textInputLayoutPapel);
        localDaClinica = findViewById(R.id.textViewLocalizacaoDaClinica);
        exemploLocal = findViewById(R.id.textViewOndeBuscarLocalizacao);
        representaY = findViewById(R.id.textViewY);
        representaX = findViewById(R.id.textViewX);

        autoCompleteTextViewPapel = findViewById(R.id.auto_complete_text);
        adapterItemsPapel = new ArrayAdapter<>(this, R.layout.list_item_layout, papel);

        autoCompleteTextViewPapel.setAdapter(adapterItemsPapel);

        autoCompleteTextViewPapel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textInputLayoutPapel.setHintEnabled(false);
            }
        });
        autoCompleteTextViewPapel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //textInputLayoutPapel.setHintEnabled(false);
                String item = adapterView.getItemAtPosition(i).toString();
                escolha = item;
                Toast.makeText(CadastroActivity.this, "Opção escolhida: "+item, Toast.LENGTH_SHORT).show();
                if(item.equals("Médico")){
                    selectCard.setVisibility(selectCard.VISIBLE);
                    campoConvenio.setVisibility(campoConvenio.VISIBLE);
                    campoCrm.setVisibility(campoCrm.VISIBLE);
                    campoNomeClinica.setVisibility(campoNomeClinica.VISIBLE);
                    campoTelefoneClinica.setVisibility(campoTelefoneClinica.VISIBLE);
                    localDaClinica.setVisibility(localDaClinica.VISIBLE);
                    campoCoordenadaX.setVisibility(campoCoordenadaX.VISIBLE);
                    campoCoordenadaY.setVisibility(campoCoordenadaY.VISIBLE);
                    exemploLocal.setVisibility(exemploLocal.VISIBLE);
                    representaY.setVisibility(representaY.VISIBLE);
                    representaX.setVisibility(representaX.VISIBLE);
                }else{
                    selectCard.setVisibility(selectCard.GONE);
                    campoConvenio.setVisibility(campoConvenio.GONE);
                    campoCrm.setVisibility(campoCrm.GONE);
                    campoNomeClinica.setVisibility(campoNomeClinica.GONE);
                    campoTelefoneClinica.setVisibility(campoTelefoneClinica.GONE);
                    localDaClinica.setVisibility(localDaClinica.GONE);
                    campoCoordenadaX.setVisibility(campoCoordenadaX.GONE);
                    campoCoordenadaY.setVisibility(campoCoordenadaY.GONE);
                    exemploLocal.setVisibility(exemploLocal.GONE);
                    representaY.setVisibility(representaY.GONE);
                    representaX.setVisibility(representaX.GONE);
                }
            }
        });

        //selectCard = findViewById(R.id.selectCard);
        especialidade = findViewById(R.id.especialidades);
        selectedEsps = new boolean[especialidades.length];

        selectCard.setOnClickListener(v -> {
            showEspecialidadesDialog();
        });

        convenio = findViewById(R.id.convenios);
        selectedConvs = new boolean[convenios.length];

        campoConvenio.setOnClickListener(v -> {
            showConveniosDialog();
        });
    }

    private void showEspecialidadesDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(CadastroActivity.this);

        builder.setTitle("Selecione suas especialidades");
        builder.setCancelable(false);

        builder.setMultiChoiceItems(especialidades, selectedEsps, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                if(isChecked){
                    if(listaEspecialidades.size() < 3){
                        if(!listaEspecialidades.contains(which)) {
                            listaEspecialidades.add(which);
                        }
                    }else{
                        Toast.makeText(CadastroActivity.this, "Por motivos de segurança, só permitimos a seleção de no máximo 3 especialidades", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    /*if(which <= listaEspecialidades.size()){
                        //selectedEsps[which] = false;
                        listaEspecialidades.remove(which);
                    }*/
                    if(listaEspecialidades.contains(which)){
                        listaEspecialidades.remove(listaEspecialidades.indexOf(which));
                    }
                    selectedEsps[which] = false;
                    //listaEspecialidades.remove(which);
                    //listaEspecialidades.remove(listaEspecialidades.indexOf(which));
                }
            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                StringBuilder stringBuilder = new StringBuilder();
                for(int i = 0; i < listaEspecialidades.size(); ++i){
                    stringBuilder.append(especialidades[listaEspecialidades.get(i)]);
                    listaEsps.add(especialidades[listaEspecialidades.get(i)]);
                    if(i != listaEspecialidades.size()-1){
                        stringBuilder.append(", ");
                    }
                    especialidade.setText(stringBuilder.toString());
                }
            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        }).setNeutralButton("Limpar tudo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                for(int i = 0; i < selectedEsps.length; ++i){
                    selectedEsps[i] = false;
                    listaEspecialidades.clear();
                    listaEsps.clear();
                    especialidade.setText("");
                }
            }
        });

        builder.show();
    }

    private void showConveniosDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(CadastroActivity.this);

        builder.setTitle("Selecione seus convênios");
        builder.setCancelable(false);

        builder.setMultiChoiceItems(convenios, selectedConvs, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                if(isChecked){
                    if(listaConvenios.size() < 3){
                        if(!listaConvenios.contains(which)) {
                            listaConvenios.add(which);
                        }
                    }else{
                        Toast.makeText(CadastroActivity.this, "Por motivos de segurança, só permitimos a seleção de no máximo 3 convênios", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if(listaConvenios.contains(which)){
                        listaConvenios.remove(listaConvenios.indexOf(which));
                    }
                    selectedConvs[which] = false;
                }
            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                StringBuilder stringBuilder = new StringBuilder();
                for(int i = 0; i < listaConvenios.size(); ++i){
                    stringBuilder.append(convenios[listaConvenios.get(i)]);
                    listaConvs.add(convenios[listaConvenios.get(i)]);
                    if(i != listaConvenios.size()-1){
                        stringBuilder.append(", ");
                    }
                    convenio.setText(stringBuilder.toString());
                }
            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        }).setNeutralButton("Limpar tudo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                for(int i = 0; i < selectedConvs.length; ++i){
                    selectedConvs[i] = false;
                    listaConvenios.clear();
                    listaConvs.clear();
                    convenio.setText("");
                }
            }
        });

        builder.show();
    }

    public void validarCampos(View v){
        nome = campoNome.getText().toString();
        email = campoEmail.getText().toString();
        phone = campoPhone.getText().toString();
        cpf = campoCpf.getText().toString();
        senha = campoSenha.getText().toString();
        confSenha = campoConfSenha.getText().toString();

        crm = campoCrm.getText().toString();
        nomeDaClinica = campoNomeClinica.getText().toString();
        teleDaClinica = campoTelefoneClinica.getText().toString();
        coordX = campoCoordenadaX.getText().toString();
        coordY = campoCoordenadaY.getText().toString();

        if(verNome() && phone.length() == 11 && verTele(phone)){
            //if(phone.length() == 11 && verTele(phone)){
                if(cpf.length() == 14 && verNums(cpf)){
                    if(!email.isEmpty()){
                        if(senha.length() >= 8){
                            if(!confSenha.equals(senha)){
                                Toast.makeText(this, "Preencha o campo Confirme sua senha corretamente", Toast.LENGTH_SHORT).show();
                            }else{

                                if(!escolha.isEmpty() && escolha.equals("Médico")){
                                    if(verCrm(crm) && verLista(listaEsps) && verLista(listaConvs) && verNomeClinica(nomeDaClinica) && teleDaClinica.length() == 11 && verTele(teleDaClinica) && verCoordenadas(coordX, coordY)){
                                        usuario = new Usuario();
                                        usuario.setNome(nome);
                                        usuario.setTelefone(phone);
                                        usuario.setCpf(cpf);
                                        usuario.setEmail(email);
                                        usuario.setSenha(senha);

                                        usuario.setPapel(escolha);
                                        usuario.setEspecialidades(listaEsps);
                                        usuario.setConvenios(listaConvs);
                                        usuario.setCrm(crm);
                                        usuario.setNomeDaClinica(nomeDaClinica);
                                        usuario.setTeleDaClinica(teleDaClinica);
                                        usuario.setCoordX(coordX);
                                        usuario.setCoordY(coordY);
                                    }
                                }else if(!escolha.isEmpty() && escolha.equals("Paciente")){
                                    usuario = new Usuario();
                                    usuario.setNome(nome);
                                    usuario.setTelefone(phone);
                                    usuario.setCpf(cpf);
                                    usuario.setEmail(email);
                                    usuario.setSenha(senha);

                                    usuario.setPapel(escolha);
                                    usuario.setEspecialidades(null);
                                    usuario.setConvenios(null);
                                    usuario.setCrm("");
                                    usuario.setNomeDaClinica("");
                                    usuario.setTeleDaClinica("");
                                    usuario.setCoordX("");
                                    usuario.setCoordY("");
                                }else{
                                    Toast.makeText(this, "Por favor selecione o papel que deseja representar nesta aplicação", Toast.LENGTH_SHORT).show();
                                }

                                cadastrarUsuario();
                            }
                        }else{
                            Toast.makeText(this, "Preencha o campo Senha com no mínimo 8 dígitos e no máximo 16", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(this, "Preencha o campo E-mail", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "Preencha o campo CPF corretamente no formato XXX.XXX.XXX-XX", Toast.LENGTH_SHORT).show();
                }
            /*}else{
                Toast.makeText(this, "Preencha o campo Telefone segundo o modelo DDXXXXXXXXX", Toast.LENGTH_SHORT).show();
            }*/
        }else{
            Toast.makeText(this, "Preencha o campo Nome", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean verNome() {
        for(int i = 0; i < nome.length(); ++i) {
            char ch = nome.charAt(i);

            if(i < nome.length()-1){
                char ch1 = nome.charAt(i + 1);

                if (ch == ' ' && ch1 == ' ') {
                    return false;
                }
            }
        }

        if (nome.length() == 0) {
            return false;
        } else {
            for (int i = 0; i < nome.length(); ++i) {
                char ch = nome.charAt(i);

                if (!((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= 'á' && ch <= 'ú') || (ch >= 'Á' && ch <= 'Ú') || ch == 'ã' || ch == 'õ' || ch == ' ') || (i == 0 && ch == ' ')) {
                    Toast.makeText(this, "Preencha o campo Nome apropriadamente (Verifique espaços indesejados)", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }

        return true;
    }

    private boolean verTele(String phone){
        for (int i = 0; i < phone.length(); ++i) {
            char ch = phone.charAt (i);

            if(i == 0 && ch != '8'){
                Toast.makeText(this, "O número de telefone deve estar de acordo segundo o modelo: DDXXXXXXXXX", Toast.LENGTH_SHORT).show();
                return false;
            }else if (!((ch >= '0' && ch <= '9'))) {
                Toast.makeText(this, "O número de telefone deve estar de acordo segundo o modelo: DDXXXXXXXXX", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }

    private boolean verNums(String cpf){
        for (int i = 0; i < cpf.length(); ++i) {
            char ch = cpf.charAt (i);

            if (((i == 3 || i == 7) && ch != '.') || (i == 11 && ch != '-')) {
                return false;
            } else if (!((ch >= '0' && ch <= '9') || ch == '.' || ch == '-')) {
                return false;
            }
        }

        return true;
    }

    private boolean verCrm(String crm){
        if(crm.isEmpty() || crm.length() != 6){
            Toast.makeText(this, "O CRM precisa ter exatamente 6 dígitos", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean verLista(ArrayList<String> lista){
        if(lista.isEmpty()){
            Toast.makeText(this, "Por favor não esqueça de informar suas especialidades e convênios", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean verNomeClinica(String nomeClinica){
        for(int i = 0; i < nome.length(); ++i) {
            char ch = nome.charAt(i);

            if(i < nome.length()-1){
                char ch1 = nome.charAt(i + 1);

                if (ch == ' ' && ch1 == ' ') {
                    Toast.makeText(this,"Por favor evite o uso de caracteres especiais no campo Nome Da Clínica e verifique espaços indesejados (Exemplos: !,?,/,>,<,(,),*,&,%,$,#,@)", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }

        if (nome.length() == 0) {
            return false;
        } else {
            for (int i = 0; i < nome.length(); ++i) {
                char ch = nome.charAt(i);

                if (!((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= 'á' && ch <= 'ú') || (ch >= 'Á' && ch <= 'Ú') || ch == 'ã' || ch == 'õ' || ch == ' ') || (ch >= '0' && ch <= '9') || (i == 0 && ch == ' ')) {
                    Toast.makeText(this, "Por favor evite o uso de caracteres especiais no campo Nome Da Clínica e verifique espaços indesejados (Exemplos: !,?,/,>,<,(,),*,&,%,$,#,@)", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }

        return true;
    }

    private boolean verCoordenadas(String coordenadaX, String coordenadaY){
        if(coordenadaX.isEmpty() || coordenadaY.isEmpty()){
            Toast.makeText(this, "Preencha TODOS os campos de Coordenada", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            double coordX = Double.parseDouble(coordenadaX);
            double coordY = Double.parseDouble(coordenadaY);

            //Extremos de Quixadá
            //  Coord Y                         Coord X
            //-4.950165 Y extremo de cima,    -39.012800
            //-4.990696 Y extremo de baixo,   -39.019151
            //-4.965386,                      -39.040437 X extremo da esquerda
            //-4.973253,                      -38.996148 X extremo da direita

            if(coordY > -4.950165 || coordY < -4.990696){
                Toast.makeText(this, "Coordenada Y fora do alcance de Quixadá", Toast.LENGTH_SHORT).show();
                return false;
            }else if(coordX > -38.996148 || coordX < -39.040437){
                Toast.makeText(this, "Coordenada X fora do alcance de Quixadá", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }

    private void cadastrarUsuario() {
        autenticacao = ConfiguraBD.FirebaseAutenticacao();

        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(CadastroActivity.this, "Sucesso ao cadastrar o usuário", Toast.LENGTH_SHORT).show();

                    FirebaseUser user = autenticacao.getCurrentUser();
                    updateUI(user);

                }else{
                    String excecao;

                    try{
                        throw Objects.requireNonNull(task.getException());
                    }catch(FirebaseAuthWeakPasswordException e){
                        excecao = "Digite uma senha mais forte";
                    }catch(FirebaseAuthInvalidCredentialsException e){
                        excecao = "Digite um email válido";
                    }catch(FirebaseAuthUserCollisionException e){
                        excecao = "Esta conta já existe";
                    }catch(Exception e){
                        excecao = "Erro ao cadastrar usuário " + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroActivity.this, excecao, Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {

            Map<String, Object> doc = new HashMap<>();
            doc.put("id", user.getUid());
            doc.put("nome", usuario.getNome());
            doc.put("cpf", usuario.getCpf());
            doc.put("email", usuario.getEmail());
            doc.put("telefone", usuario.getTelefone());
            doc.put("senha", usuario.getSenha());

            db.collection("Usuario").document(user.getUid()).set(doc)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(CadastroActivity.this, "Dados armazenados...", Toast.LENGTH_SHORT).show();
                                    abrirHome();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(CadastroActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                    });
        } else {
            Toast.makeText(this, "Usuário nulo", Toast.LENGTH_SHORT).show();
        }


    }

    private void abrirHome() {
        Intent intent = new Intent(this, Home.class);

        startActivity(intent);

        finish();
    }

    public void voltarCadastro(View v){
        finish();
    }

}