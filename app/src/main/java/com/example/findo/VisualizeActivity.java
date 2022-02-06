package com.example.findo;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentOnAttachListener;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.ar.core.Anchor;
import com.google.ar.core.Config;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Pose;
import com.google.ar.core.Session;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.SceneView;
import com.google.ar.sceneform.Sceneform;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class VisualizeActivity extends AppCompatActivity implements
        FragmentOnAttachListener,
        BaseArFragment.OnTapArPlaneListener,
        BaseArFragment.OnSessionConfigurationListener,
        ArFragment.OnViewCreatedListener {

    private ArFragment arFragment;
    private Renderable model;
    private ViewRenderable viewRenderable;
//    private ImageView imageAR;
    private TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_visualize);
        getSupportFragmentManager().addFragmentOnAttachListener(this);

        if (savedInstanceState == null) {
            if (Sceneform.isSupported(this)) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.arFragment, ArFragment.class, null)
                        .commit();
            }
        }

//        arFragment = (CustomArFragment) getSupportFragmentManager().findFragmentById(R.id.sceneform_fragment);
//        arFragment.getPlaneDiscoveryController().hide();
//        arFragment.getArSceneView().getScene().addOnUpdateListener(this::onUpdateFrame);
loadModels();
    }


    private void onUpdateFrame(FrameTime frameTime) {
//        Frame frame = arFragment.getArSceneView().getArFrame();
        Bundle bundle = getIntent().getExtras();
        Log.d("TW_onUpdateFrame","onUpdateFrame");
        String t = bundle.getString("image_key");
        txt.setText(t);
        loadModels();
    }

    @Override
    public void onAttachFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment) {
        if (fragment.getId() == R.id.arFragment) {
            arFragment = (ArFragment) fragment;
            arFragment.setOnSessionConfigurationListener(this);
            arFragment.setOnViewCreatedListener(this);
            arFragment.setOnTapArPlaneListener(this);
        }
    }

    @Override
    public void onSessionConfiguration(Session session, Config config) {
        if (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
            config.setDepthMode(Config.DepthMode.AUTOMATIC);
        }
    }

    @Override
    public void onViewCreated(ArSceneView arSceneView) {
        arFragment.setOnViewCreatedListener(null);

        // Fine adjust the maximum frame rate
        arSceneView.setFrameRateFactor(SceneView.FrameRate.FULL);
    }

    public void loadModels() {
        WeakReference<VisualizeActivity> weakActivity = new WeakReference<>(this);

        ModelRenderable.builder()
                .setSource(this, Uri.parse("https://firebasestorage.googleapis.com/v0/b/findo-d605f.appspot.com/o/images%2Fproduct%2FSpatu.png?alt=media&token=e04ef9f9-6cde-469c-9139-15a0425ace66"))
//                  .setSource(this, Uri.parse("https://firebasestorage.googleapis.com/v0/b/findo-d605f.appspot.com/o/images%2Ftesting.glb?alt=media&token=cf4c67cf-aaff-4a1b-b603-6277b0440f31"))
//                .setSource(this, Uri.parse("https://storage.googleapis.com/ar-answers-in-search-models/static/Tiger/model.glb"))
                .setIsFilamentGltf(true)
                .setAsyncLoadEnabled(true)
                .build()
                .thenAccept(model -> {
                    VisualizeActivity activity = weakActivity.get();
                    if (activity != null) {
                        activity.model = model;
                    }
                })
                .exceptionally(throwable -> {
                    Toast.makeText(
                            this, "Unable to load model", Toast.LENGTH_LONG).show();
                    return null;
                });
        ViewRenderable.builder()
                .setView(this, R.layout.layout_ar_hit)
                .build()
                .thenAccept(viewRenderable -> {
                    VisualizeActivity activity = weakActivity.get();
                    if (activity != null) {
                        activity.viewRenderable = viewRenderable;
                        Bundle bundle = getIntent().getExtras();
                        ImageView imageAR = (ImageView) viewRenderable.getView();
                        Picasso.get().load(bundle.getString("image_key")).into(imageAR);
                    }
                })
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load model", Toast.LENGTH_LONG).show();
                    return null;
                });
    }

    @Override
    public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {
        if (model == null || viewRenderable == null) {
            Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create the Anchor.

        Anchor anchor = hitResult.createAnchor();
        AnchorNode anchorNode = new AnchorNode(anchor);
        anchorNode.setParent(arFragment.getArSceneView().getScene());

        // Create the transformable model and add it to the anchor.
//        TransformableNode model = new TransformableNode(arFragment.getTransformationSystem());
//        model.getScaleController().setMaxScale(0.03f);
//        model.getScaleController().setMinScale(0.02f);
////        anchorNode.getUp();
////        model.setLookDirection(Vector3.back());
////        model.setLocalRotation(Quaternion.axisAngle(new Vector3(1f, 0f, 0f), -90f));
////        model.setLocalPosition(new Vector3(0.0f,0.8f,0.0f));
//        model.setLocalRotation(Quaternion.axisAngle(new Vector3(-1.5f, -1.7f, -0.8f), -90f));
//        model.setParent(anchorNode);
//        model.setRenderable(this.model)
//                .animate(true).start();
//        model.select();

        Node model = new Node();
        model.setParent(anchorNode);
//        model.setLocalPosition(new Vector3(0.0f, 0.0f, 0.0f));
//        model.setLocalRotation(Quaternion.axisAngle(new Vector3(-1.0f, -1.0f, 0.0f), -90f));
//        model.getScaleController().setMinScale(0.02f);
//        titleNode.setLocalRotation(Quaternion.axisAngle(new Vector3(-1f, 0, 0), 90f));
        model.setRenderable(viewRenderable);
        model.setEnabled(true);
    }
}