package com.example.screenlockpattern

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.screenlockpattern.componet.PatternViewStageState
import com.example.screenlockpattern.componet.PatternViewState
import com.example.screenlockpattern.viewmodel.PatternLockViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<PatternLockViewModel>()

    /** ok now run it*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        clear_TextButton.setOnClickListener {
            viewModel.updateViewState(PatternViewState.Initial)
        }

        stage_Button.setOnClickListener {
            when (pattern_LockView.stageState) {
                PatternViewStageState.FIRST -> {
                    viewModel.updateViewState(PatternViewState.Initial)

                    pattern_LockView.stageState = PatternViewStageState.SECOND
                    stage_Button.text = getString(R.string.stage_button_confirm)
                    tv_SubTitle.isInvisible = true
                }
                PatternViewStageState.SECOND -> {
                    AlertDialog.Builder(this).apply {
                        setMessage(R.string.alert_dialog_confirm_message)
                        setPositiveButton(
                            R.string.alert_dialog_positive_button
                        ) { _, _ -> }
                    }.show()
                }
            }
        }

        pattern_LockView.setOnChangeStateListener { state ->
            viewModel.updateViewState(state)
        }

        lifecycleScope.launchWhenCreated {
            viewModel.viewState.collect { patternViewState ->
                when (patternViewState) {
                    is PatternViewState.Initial -> {
                        pattern_LockView.reset()
                        stage_Button.isEnabled = false
                        clear_TextButton.isVisible = false
                        tvMessage.run {
                            text = if (pattern_LockView.stageState == PatternViewStageState.FIRST) {
                                getString(R.string.initial_message_first_stage)
                            } else {
                                getString(R.string.initial_message_second_stage)
                            }
                            setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.message_text_default_color
                                )
                            )
                        }
                    }
                    is PatternViewState.Started -> {
                        tvMessage.run {
                            text = getString(R.string.started_message)
                            setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.message_text_default_color
                                )
                            )
                        }
                    }
                    is PatternViewState.Success -> {
                        stage_Button.isEnabled = true
                        tvMessage.run {
                            text = if (pattern_LockView.stageState == PatternViewStageState.FIRST) {
                                getString(R.string.success_message_first_stage)
                            } else {
                                getString(R.string.success_message_second_stage)
                            }
                            setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.message_text_default_color
                                )
                            )
                        }
                        clear_TextButton.isVisible =
                            pattern_LockView.stageState == PatternViewStageState.FIRST
                    }
                    is PatternViewState.Error -> {
                        tvMessage.run {
                            text = if (pattern_LockView.stageState == PatternViewStageState.FIRST) {
                                getString(R.string.error_message_first_stage)
                            } else {
                                getString(R.string.error_message_second_stage)
                            }
                            setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.message_text_error_color
                                )
                            )
                        }
                        clear_TextButton.isVisible =
                            pattern_LockView.stageState == PatternViewStageState.FIRST
                    }
                }
            }
        }
    }
}