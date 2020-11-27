package com.openclassrooms.realestatemanager.controller;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoanSimulatorActivity extends AppCompatActivity {

    @BindView(R.id.activity_loan_simulator_editext_monthly_payment_total) EditText mEditTextMonthlyPaymentTotal;
    @BindView(R.id.activity_loan_simulator_edittext_insurance_rate) EditText mEditTextInsuranceRate;
    @BindView(R.id.activity_loan_simulator_edittext_interest_rate) EditText mEditTextInterestRate;
    @BindView(R.id.activity_loan_simulator_textview_cost_total) TextView mTextViewCostTotal;
    @BindView(R.id.activity_loan_simulator_textview_amount) TextView mTextViewAmount;
    @BindView(R.id.activity_loan_simulator_textview_during) TextView mTextViewDuring;
    @BindView(R.id.activity_loan_simulator_textview_monthly_payment_insurance) TextView mTextViewMonthlyPaymentInsurance;
    @BindView(R.id.activity_loan_simulator_textview_monthly_payment_bank) TextView mTextViewMonthlyPaymentBank;
    @BindView(R.id.activity_loan_simulator_seekbar_amount) SeekBar mSeekBarAmount;
    @BindView(R.id.activity_loan_simulator_seekbar_during) SeekBar mSeekBarDuring;

    String devise = "€";
    int amount = 10;
    int during = 5;
    double interestRate = 2.96;
    double insuranceRate = 0.84;
    double monthlyPaymentTotal  = 186.51;
    double monthlyPaymentInsurance = 7;
    double monthlyPaymentBank = 12.842;
    double costTotal = 1190.552;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_simulator);
        ButterKnife.bind(this);

        updateUI();

        mSeekBarAmount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                if (mSeekBarAmount.getProgress() <= 0){
                    amount = 1;
                } else {
                    amount = mSeekBarAmount.getProgress();
                }

                updateSimulator();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mSeekBarDuring.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                during = mSeekBarDuring.getProgress();
                updateSimulator();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mEditTextInterestRate.setOnEditorActionListener((textView, i, keyEvent) -> {
            String interestRateEdit = mEditTextInterestRate.getText().toString();
            interestRate = Double.parseDouble(interestRateEdit.substring(0, interestRateEdit.length() -2));

            updateSimulator();

            return false;
        });

        mEditTextInsuranceRate.setOnEditorActionListener((textView, i, keyEvent) -> {
            String insuranceRateEdit = mEditTextInsuranceRate.getText().toString();
            insuranceRate = Double.parseDouble(insuranceRateEdit.substring(0, insuranceRateEdit.length() -2));

            updateSimulator();

            return false;
        });
    }

    private void updateSimulator(){
        updateData();
        updateUI();
    }

    private void updateData(){
//        amount = mSeekBarAmount.getProgress();
//        during = mSeekBarDuring.getProgress();
//        interestRate = 2.96;
//        insuranceRate = 0.84;

        monthlyPaymentTotal = Utils.calculateMonthlyPaymentTotal(amount * 1000, interestRate / 100, insuranceRate / 100,during * 12);
        monthlyPaymentInsurance = Utils.calculateMonthlyPaymentInsuranceOnly(amount * 1000, insuranceRate / 100);
        monthlyPaymentBank = Utils.calculateMonthlyPaymentInterestBankOnly(amount * 1000, interestRate / 100, during * 12);
        costTotal = Utils.calculateCostTotal(amount * 1000, interestRate / 100, insuranceRate / 100, during * 12);
        Log.d("TAG", "updateData: ");
    }

    private void updateUI(){
        mTextViewCostTotal.setText(String.format("%.2f %s", costTotal, devise));
        mEditTextInsuranceRate.setText(String.format("%s", insuranceRate + " %"));
        mEditTextInterestRate.setText(String.format("%s", interestRate + " %"));
        mEditTextMonthlyPaymentTotal.setText(String.format("%.2f %s/mois", monthlyPaymentTotal, devise));

        mTextViewAmount.setText(String.format("%d 000 %s", amount, devise));
        mTextViewDuring.setText(String.format("%d ans", during));
        mTextViewMonthlyPaymentInsurance.setText(String.format("%.2f %s/mois", monthlyPaymentInsurance, devise));
        mTextViewMonthlyPaymentBank.setText(String.format("%.2f %s/mois", monthlyPaymentBank, devise));

        mSeekBarAmount.setProgress(amount);
        mSeekBarDuring.setProgress(during);
    }

    private void calculateTAEG(){
        int nbOfMonths = during * 12; // 180 mois
        //
        //TAEG =  [(costTotal – amount) / amount] × nbOfMonths.
        
    }
}