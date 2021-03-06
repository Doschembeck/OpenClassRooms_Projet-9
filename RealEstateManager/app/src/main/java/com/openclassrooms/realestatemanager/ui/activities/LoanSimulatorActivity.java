package com.openclassrooms.realestatemanager.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.SeekBar;
import com.openclassrooms.realestatemanager.databinding.ActivityLoanSimulatorBinding;
import com.openclassrooms.realestatemanager.model.Devise;
import com.openclassrooms.realestatemanager.utils.Constants;
import com.openclassrooms.realestatemanager.utils.FormatUtils;
import com.openclassrooms.realestatemanager.utils.SimulatorUtils;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class LoanSimulatorActivity extends AppCompatActivity {

    private ActivityLoanSimulatorBinding binding;
    private SharedPreferences mSharedPreferences;
    private final int INTERVAL_SEEKBAR = 1000;

    Devise devise;
    int costProperty = 210000;
    int amountContribution = 0;
    int amountLoan = 0;
    int duringInMonth = 240;
    double interestRate = 1.25;
    double insuranceRate = 0.36;
    double monthlyPaymentTotal  = 0;
    double monthlyPaymentInterest = 0;
    double monthlyPaymentInsurance = 0;
    double costTotal = 0;
    double costTotalInterestAndInsurance = 0;
    double costTotalInterest = 0;
    double costTotalInsurance = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoanSimulatorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mSharedPreferences = Utils.getSharedPreferences(this);
        devise = Utils.getCurrentDevise(mSharedPreferences);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("amount_property")){
            costProperty = (int) intent.getExtras().getDouble("amount_property", 0);
        }

        binding.toolbarOnlyback.setOnClickListener(view -> onBackPressed());

        setMaxSeekbar();

        updateSimulator();

        binding.activityLoanSimulatorSeekbarAmount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                costProperty = seekBar.getProgress() * INTERVAL_SEEKBAR + amountContribution;
                updateSimulator();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        binding.activityLoanSimulatorSeekbarDuring.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                duringInMonth = seekBar.getProgress();
                updateSimulator();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        binding.activityLoanSimulatorSeekbarAmountContribution.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                amountContribution = seekBar.getProgress() * INTERVAL_SEEKBAR;
                updateSimulator();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        binding.activityLoanSimulatorSeekbarCostProperty.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                costProperty = binding.activityLoanSimulatorSeekbarCostProperty.getProgress() * INTERVAL_SEEKBAR;
                updateSimulator();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.activityLoanSimulatorEdittextAmount.setOnFocusChangeListener((view, b) -> {
            if (!b){
                try {
                    costProperty = Integer.parseInt(binding.activityLoanSimulatorEdittextAmountContribution.getText().toString()
                            .replaceAll("\\s", "")
                            .replaceAll(devise.getSymbole(), "")) + amountContribution;

                } catch (NumberFormatException e) {
                    costProperty = 10000;
                }

                updateSimulator();
            }
        });
        binding.activityLoanSimulatorEdittextAmountContribution.setOnFocusChangeListener((view, b) -> {
            if(!b){
                try {
                    amountContribution = Integer.parseInt(binding.activityLoanSimulatorEdittextAmountContribution.getText().toString()
                            .replaceAll("\\s", "")
                            .replaceAll(devise.getSymbole(), ""));

                } catch (NumberFormatException e) {
                    amountContribution = 0;
                }

                updateSimulator();

            }
        });
        binding.activityLoanSimulatorEdittextCostProperty.setOnFocusChangeListener((view, b) -> {
            if (!b){
                try {
                    costProperty = Integer.parseInt(binding.activityLoanSimulatorEdittextCostProperty.getText().toString()
                            .replaceAll("\\s", "")
                            .replaceAll(devise.getSymbole(), ""));

                } catch (NumberFormatException e) {
                    costProperty = 10000;
                }

                updateSimulator();

            }
        });
        binding.activityLoanSimulatorEdittextInterestRate.setOnFocusChangeListener((view, b) -> {
            if (!b){
                try {
                    interestRate = Double.parseDouble(binding.activityLoanSimulatorEdittextInterestRate.getText().toString()
                            .replaceAll("\\s","")
                            .replaceAll("%", ""));

                } catch (NumberFormatException e) {
                    interestRate = 0.01;
                }

                updateSimulator();

            }
        });
        binding.activityLoanSimulatorEdittextInsuranceRate.setOnFocusChangeListener((view, b) -> {
            if (!b) {
                try {
                    insuranceRate = Double.parseDouble(binding.activityLoanSimulatorEdittextInsuranceRate.getText().toString()
                            .replaceAll("\\s", "")
                            .replaceAll("%", ""));

                } catch (NumberFormatException e) {
                    insuranceRate = 0;
                }

                updateSimulator();

            }
        });

    }

    private void setMaxSeekbar(){
        binding.activityLoanSimulatorSeekbarCostProperty.setMax(600000 / INTERVAL_SEEKBAR);
        binding.activityLoanSimulatorSeekbarAmount.setMax(600000 / INTERVAL_SEEKBAR);
        binding.activityLoanSimulatorSeekbarAmountContribution.setMax(200000 / INTERVAL_SEEKBAR);
    }

    private void updateSimulator(){
        updateData();
        updateUI();
    }

    private void updateData(){
        amountLoan = costProperty - amountContribution;

        monthlyPaymentInsurance = SimulatorUtils.calculateMonthlyPaymentInsuranceOnly(amountLoan, insuranceRate / 100);
        monthlyPaymentInterest = SimulatorUtils.calculateMonthlyPaymentInterestBankOnly(amountLoan, interestRate / 100, duringInMonth);
        monthlyPaymentTotal = (monthlyPaymentInsurance + monthlyPaymentInterest) + amountLoan / duringInMonth;

        costTotalInsurance = monthlyPaymentInsurance * (duringInMonth);
        costTotalInterest = monthlyPaymentInterest * (duringInMonth);
        costTotalInterestAndInsurance =  costTotalInterest + costTotalInsurance;
        costTotal = costTotalInterestAndInsurance + amountLoan;

    }

    private void updateUI(){
        NumberFormat nf = NumberFormat.getInstance();

        binding.activityLoanSimulatorEdittextInsuranceRate.setText(String.format("%s", insuranceRate + " %"));
        binding.activityLoanSimulatorEdittextInterestRate.setText(String.format("%s", interestRate + " %"));
        binding.activityLoanSimulatorEditextMonthlyPaymentTotal.setText(String.format("%s/mois", FormatUtils.formatEditTextWithDevise(monthlyPaymentTotal, devise)));;

        binding.activityLoanSimulatorEdittextAmount.setText(FormatUtils.formatEditTextWithDevise(amountLoan, devise));
        binding.activityLoanSimulatorTextviewDuring.setText(String.format("%d mois (%s ans)", duringInMonth, new DecimalFormat("#.#").format(duringInMonth / 12D)));
        binding.activityLoanSimulatorTextviewMonthlyPaymentInsurance.setText(String.format("%s/mois", FormatUtils.formatEditTextWithDevise(monthlyPaymentInsurance, devise)));
        binding.activityLoanSimulatorTextviewMonthlyPaymentBank.setText(String.format("%s/mois", FormatUtils.formatEditTextWithDevise(monthlyPaymentInterest, devise)));
        binding.activityLoanSimulatorEdittextAmountContribution.setText(FormatUtils.formatEditTextWithDevise(amountContribution, devise));
        binding.activityLoanSimulatorEdittextCostProperty.setText(FormatUtils.formatEditTextWithDevise(costProperty, devise));

        binding.activityLoanSimulatorTextviewCostTotalInterest.setText(FormatUtils.formatEditTextWithDevise(costTotalInterest, devise));
        binding.activityLoanSimulatorTextviewCostTotalInsurance.setText(FormatUtils.formatEditTextWithDevise(costTotalInsurance, devise));
        binding.activityLoanSimulatorTextviewCostTotalInterestAndInsurance.setText(FormatUtils.formatEditTextWithDevise(costTotalInterestAndInsurance, devise));
        binding.activityLoanSimulatorTextviewCostTotal.setText(FormatUtils.formatEditTextWithDevise(costTotal, devise));

        binding.activityLoanSimulatorSeekbarCostProperty.setProgress(costProperty / INTERVAL_SEEKBAR);
        binding.activityLoanSimulatorSeekbarAmountContribution.setProgress(amountContribution / INTERVAL_SEEKBAR);
        binding.activityLoanSimulatorSeekbarAmount.setProgress(amountLoan / INTERVAL_SEEKBAR);
        binding.activityLoanSimulatorSeekbarDuring.setProgress(duringInMonth);
    }

}