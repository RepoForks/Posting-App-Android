package com.app.infideap.postingapp.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.app.infideap.notificationapp.R;
import com.app.infideap.postingapp.entity.Post;
import com.app.infideap.postingapp.resource.Api;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Shiburagi on 12/02/2017.
 */

public class CommentBottomSheetFragment extends BottomSheetDialogFragment {


    private static final String ARG_POST = "ARG_POST";
    public static final String TAG = CommentBottomSheetFragment.class.getSimpleName();
    private Post post;
    private EditText commentEditText;
    private ImageView sendImageView;
    private BottomSheetBehavior<FrameLayout> behavior;

    public static CommentBottomSheetFragment newInstance(Post post) {
        CommentBottomSheetFragment fragment = new CommentBottomSheetFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_POST, post);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            post = (Post) getArguments().getSerializable(ARG_POST);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment_bottomsheet, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        commentEditText = (EditText) view.findViewById(R.id.editText_comment);
        sendImageView = (ImageView) view.findViewById(R.id.imageView_send);
        sendImageView.setVisibility(View.GONE);

        commentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sendImageView.setVisibility(charSequence.length() == 0 ?
                        View.GONE : View.VISIBLE
                );
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        sendImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Api.getInstance().post().add(wrap());

                commentEditText.setText(null);

            }
        });

        getChildFragmentManager().beginTransaction()
                .replace(R.id.container_list, CommentFragment.newInstance(post))
                .commit();
    }

    private Post wrap() {
        Post post = new Post();
        post.text = commentEditText.getText().toString();
        post.parentKey = this.post.key;
        post.createdBy = FirebaseAuth.getInstance().getCurrentUser().getUid();
        post.createdDate = System.currentTimeMillis();
        post.updatedDate = post.createdDate;
        return post;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;

                final FrameLayout bottomSheet = (FrameLayout) d.findViewById(android.support.design.R.id.design_bottom_sheet);
                behavior = BottomSheetBehavior.from(bottomSheet);
                bottomSheet.setBackgroundColor(Color.argb(0, 0, 0, 0));
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            }
        });

        return dialog;
    }
}
