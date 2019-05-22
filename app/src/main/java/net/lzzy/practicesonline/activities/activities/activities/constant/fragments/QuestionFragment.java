package net.lzzy.practicesonline.activities.activities.activities.constant.fragments;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import net.lzzy.practicesonline.R;
import net.lzzy.practicesonline.activities.activities.activities.constant.models.FavoriteFactory;
import net.lzzy.practicesonline.activities.activities.activities.constant.models.Option;
import net.lzzy.practicesonline.activities.activities.activities.constant.models.Question;
import net.lzzy.practicesonline.activities.activities.activities.constant.models.QuestionFactory;
import net.lzzy.practicesonline.activities.activities.activities.constant.models.UserCookies;
import net.lzzy.practicesonline.activities.activities.activities.constant.models.view.QuestionType;

import java.util.List;

/**
 * Created by lzzy_gxy on 2019/4/30.
 * Description:
 */
public class QuestionFragment extends BaseFragment {
    private static final String ARG_QUESTION_ID = "argQuestionId";
    private static final String ARG_POS = "argPos";
    private static final String ARG_IS_COMMITTED = "argIsCommitted";
    private Question question;
    private int pos;
    private boolean isCommitted;
    private TextView tvType;
    private ImageButton imgFavorite;
    private TextView tvContent;
    private RadioGroup rgOptions;
    private boolean isMulti = false;

    public static QuestionFragment newInstance(String questionId, int pos, boolean isCommitted) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUESTION_ID, questionId);
        args.putInt(ARG_POS, pos);
        args.putBoolean(ARG_IS_COMMITTED, isCommitted);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pos = getArguments().getInt(ARG_POS);
            isCommitted = getArguments().getBoolean(ARG_IS_COMMITTED);
            question = QuestionFactory.getInstance().getById(getArguments().getString(ARG_QUESTION_ID));
        }
    }

    @Override
    protected void Populate() {
        initViews();
        displayQuestion();
        generateOptions();

    }

    private void generateOptions() {
        List<Option> options = question.getOptions();
        for (Option option : options) {
            CompoundButton btn = isMulti ? new CheckBox(getContext()) : new RadioButton(getContext());
            String content = option.getLabel() + "." + option.getContent();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                btn.setButtonTintList(ColorStateList.valueOf(Color.GRAY));
            }
            btn.setText(content);
            btn.setEnabled(!isCommitted);
            btn.setOnCheckedChangeListener((buttonView, isChecked) -> {
                UserCookies.getInstance().changeOpionState(option,isChecked,isMulti);
            });
            rgOptions.addView(btn);
            boolean shouldCheck=UserCookies.getInstance().idOptionSelected(option);
            if (isCommitted && option.isAnswer()) {
                btn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            } else if (shouldCheck){
                btn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        }

    }

    private void displayQuestion() {
        isMulti = question.getType() == QuestionType.MULTI_CHOICE;
        int label = pos + 1;
        String qType = label + "." + question.getType().toString();
        tvType.setText(qType);
        tvContent.setText(question.getContent());
        int starId = FavoriteFactory.getInstance().isQuestionStarred(question.getId().toString()) ?
                android.R.drawable.star_on : android.R.drawable.star_off;
        imgFavorite.setImageResource(starId);
        imgFavorite.setOnClickListener(v -> switchStar());
    }

    private void switchStar() {
        FavoriteFactory factory = FavoriteFactory.getInstance();
        if (factory.isQuestionStarred(question.getId().toString())) {
            factory.cancelStarQuestion(question.getId());
            imgFavorite.setImageResource(android.R.drawable.star_off);
        } else {
            factory.starQuestion(question.getId());
            imgFavorite.setImageResource(android.R.drawable.star_on);
        }
    }

    private void initViews() {
        tvType = findViewById(R.id.fragment_question_tv_question_type);
        imgFavorite = findViewById(R.id.fragment_question_img_favorite);
        tvContent = findViewById(R.id.fragment_question_tv_content);
        rgOptions = findViewById(R.id.fragment_question_option_container);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_question;
    }

    @Override
    public void sarch(String kw) {

    }
}
