package tads.eaj.com.sharkbehavior;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.FaceDetector;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;


public class RealizaObservacaoActivity extends AppCompatActivity {


    private static final int REQUEST_CAMERA = 0;
    private static final int MAX_FACES = 50;

    private TextView valorObservadoresTextView;
    private int numeroObservadores = 25;
    private Button capturarCameraButton;
    private String userChoosenTask;
    private SeekBar observadoresSeekBar;
    private int imagemSelecionada = 0;//nenhuma
    private int estado = 0; //parado
    private String comentarios;
    private String id_bolsista;

    private FaceDetector.Face[] faces;
    private int face_count;
    String mCurrentPhotoPath;
    File photoFile = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realiza_observacao);

        id_bolsista = getIntent().getStringExtra("id_bolsista");

        //Log.i("TESTE_",id_bolsista);

        valorObservadoresTextView = (TextView) findViewById(R.id.valorObservadoresTextView);

        capturarCameraButton = (Button) findViewById(R.id.capturarCameraButton);
        capturarCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capturarImagem();
            }
        });

        observadoresSeekBar = (SeekBar) findViewById(R.id.observadoresSeekBar);
        observadoresSeekBar.setOnSeekBarChangeListener(seekBarListener);


    }

    public void resetImageViews() {
        ImageView img1 = (ImageView) findViewById(R.id.imageView1);
        img1.setImageResource(R.drawable.agua);
        ImageView img2 = (ImageView) findViewById(R.id.imageView2);
        img2.setImageResource(R.drawable.agua);
        ImageView img3 = (ImageView) findViewById(R.id.imageView3);
        img3.setImageResource(R.drawable.agua);
        ImageView img4 = (ImageView) findViewById(R.id.imageView4);
        img4.setImageResource(R.drawable.agua);
        ImageView img5 = (ImageView) findViewById(R.id.imageView5);
        img5.setImageResource(R.drawable.pedra);
        ImageView img6 = (ImageView) findViewById(R.id.imageView6);
        img6.setImageResource(R.drawable.agua);
        ImageView img7 = (ImageView) findViewById(R.id.imageView7);
        img7.setImageResource(R.drawable.agua);
        ImageView img8 = (ImageView) findViewById(R.id.imageView8);
        img8.setImageResource(R.drawable.agua);
        ImageView img9 = (ImageView) findViewById(R.id.imageView9);
        img9.setImageResource(R.drawable.agua);
    }

    public void clicaLocalTubarao(View v) {

        switch (v.getId()) {
            case R.id.imageView1: {
                imagemSelecionada = 1;
                resetImageViews();

                ImageView img1 = (ImageView) v;
                img1.setImageResource(R.drawable.aguax);
                break;
            }
            case R.id.imageView2: {
                imagemSelecionada = 2;
                resetImageViews();

                ImageView img2 = (ImageView) v;
                img2.setImageResource(R.drawable.aguax);
                break;
            }
            case R.id.imageView3: {
                imagemSelecionada = 3;
                resetImageViews();

                ImageView img3 = (ImageView) v;
                img3.setImageResource(R.drawable.aguax);
                break;
            }
            case R.id.imageView4: {
                imagemSelecionada = 4;
                resetImageViews();

                ImageView img4 = (ImageView) v;
                img4.setImageResource(R.drawable.aguax);
                break;
            }
            case R.id.imageView5: {
                imagemSelecionada = 5;
                resetImageViews();

                ImageView img5 = (ImageView) v;
                img5.setImageResource(R.drawable.pedrax);
                break;
            }
            case R.id.imageView6: {
                imagemSelecionada = 6;
                resetImageViews();

                ImageView img6 = (ImageView) v;
                img6.setImageResource(R.drawable.aguax);
                break;
            }
            case R.id.imageView7: {
                imagemSelecionada = 7;
                resetImageViews();

                ImageView img7 = (ImageView) v;
                img7.setImageResource(R.drawable.aguax);
                break;
            }
            case R.id.imageView8: {
                imagemSelecionada = 8;
                resetImageViews();

                ImageView img8 = (ImageView) v;
                img8.setImageResource(R.drawable.aguax);
                break;
            }
            case R.id.imageView9: {
                imagemSelecionada = 9;
                resetImageViews();

                ImageView img9 = (ImageView) v;
                img9.setImageResource(R.drawable.aguax);
                break;
            }
            default:
                break;
        }
    }

    public void cadastrarObservacaoClick(View v) {

        final CharSequence[] items = {"Sim", "Não"};

        AlertDialog.Builder builder = new AlertDialog.Builder(RealizaObservacaoActivity.this);
        builder.setTitle("Confirma o cadastro?");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int item) {
                if (items[item].equals("Sim")) {
                    RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    if (selectedId == R.id.paradoRadioButton) {
                        estado = 0;//parado
                    } else {
                        estado = 1;//nadando
                    }
                    TextView comentariosTextview = (TextView) findViewById(R.id.cometatariosEditText);
                    comentarios = comentariosTextview.getText().toString();

                    new insereObservacao().execute();
                } else {
                    Toast.makeText(RealizaObservacaoActivity.this, "Envio cancelado.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.show();
    }

    private class insereObservacao extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... arg0) {
            try {
                String numero_observadores = Integer.toString(numeroObservadores);
                String posicao_tubarao = Integer.toString(imagemSelecionada);
                String comportamento_tubarao = Integer.toString(estado);
                String observacoes = comentarios;
                String data_cadastro = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());

                /*
                    A URL especificada vai variar de acordo com o servidor. 10.0.2.2 é usado pois no emulador android
                    localhost ou 127.0.0.1 significa ele mesmo.

                 */

                //Log.i("TESTE_",numero_observadores+posicao_tubarao+comportamento_tubarao+observacoes+id_bolsista+data_cadastro);

                String link = "http://192.168.137.1/tubaraoproject/insereobservacao.php";
                String data = URLEncoder.encode("numero_observadores", "UTF-8") + "=" + URLEncoder.encode(numero_observadores, "UTF-8");
                data += "&" + URLEncoder.encode("posicao_tubarao", "UTF-8") + "=" + URLEncoder.encode(posicao_tubarao, "UTF-8");
                data += "&" + URLEncoder.encode("comportamento_tubarao", "UTF-8") + "=" + URLEncoder.encode(comportamento_tubarao, "UTF-8");
                data += "&" + URLEncoder.encode("observacoes", "UTF-8") + "=" + URLEncoder.encode(observacoes, "UTF-8");
                data += "&" + URLEncoder.encode("id_bolsista", "UTF-8") + "=" + URLEncoder.encode(id_bolsista, "UTF-8");
                data += "&" + URLEncoder.encode("data_cadastro", "UTF-8") + "=" + URLEncoder.encode(data_cadastro, "UTF-8");

                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write(data);
                wr.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                return sb.toString();
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("SUCESSO")) {
                Toast.makeText(RealizaObservacaoActivity.this, "Observação cadastrada com sucesso!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(RealizaObservacaoActivity.this, "Ocorreu um erro, tente novamente.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private final SeekBar.OnSeekBarChangeListener seekBarListener =
            new SeekBar.OnSeekBarChangeListener() {
                // update percent, then call calculate
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {
                    valorObservadoresTextView.setText(String.valueOf(progress));
                    numeroObservadores = progress;

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            };


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Tirar foto")) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, REQUEST_CAMERA);
                    }
                } else {
                    Toast.makeText(RealizaObservacaoActivity.this, "Permissões negadas.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void capturarImagem() {
        final CharSequence[] items = {"Tirar foto", "Cancelar"};

        AlertDialog.Builder builder = new AlertDialog.Builder(RealizaObservacaoActivity.this);
        builder.setTitle("Capturar com foto:");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(RealizaObservacaoActivity.this);
                if (items[item].equals("Tirar foto")) {
                    userChoosenTask = "Tirar foto";
                    if (result) {

                        //File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            // Error occurred while creating the File
                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(RealizaObservacaoActivity.this, "com.example.android.fileprovider", photoFile);

                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(intent, REQUEST_CAMERA);
                        }


                    }
                } else if (items[item].equals("Cancelar")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA)
                trataImagem();
        }
    }

    private void trataImagem() {

        new detectaFaces().execute();
        Toast.makeText(RealizaObservacaoActivity.this, "Processando imagem...", Toast.LENGTH_SHORT).show();

    }

    private class detectaFaces extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;

            Bitmap image565 = BitmapFactory.decodeFile(photoFile.getPath(), options);
            Log.i("TESTES_", photoFile.getPath());

            FaceDetector face_detector = new FaceDetector(image565.getWidth(), image565.getHeight(), MAX_FACES);
            faces = new FaceDetector.Face[MAX_FACES];
            face_count = face_detector.findFaces(image565, faces);
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            valorObservadoresTextView.setText(String.valueOf(face_count));
            observadoresSeekBar.setProgress(face_count);
            Toast.makeText(RealizaObservacaoActivity.this, "Número de observadores atualizado", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

}
