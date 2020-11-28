package com.openclassrooms.realestatemanager.controller;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoanSimulatorActivity extends AppCompatActivity {

    @BindView(R.id.activity_loan_simulator_editext_monthly_payment_total) TextView mTextViewMonthlyPaymentTotal;
    @BindView(R.id.activity_loan_simulator_edittext_insurance_rate) EditText mEditTextInsuranceRate;
    @BindView(R.id.activity_loan_simulator_edittext_interest_rate) EditText mEditTextInterestRate;
    @BindView(R.id.activity_loan_simulator_textview_cost_total) TextView mTextViewCostTotal;
    @BindView(R.id.activity_loan_simulator_edittext_amount) EditText mEditTextAmount;
    @BindView(R.id.activity_loan_simulator_textview_during) TextView mTextViewDuring;
    @BindView(R.id.activity_loan_simulator_textview_monthly_payment_insurance) TextView mTextViewMonthlyPaymentInsurance;
    @BindView(R.id.activity_loan_simulator_textview_monthly_payment_bank) TextView mTextViewMonthlyPaymentBank;
    @BindView(R.id.activity_loan_simulator_seekbar_amount) SeekBar mSeekBarAmount;
    @BindView(R.id.activity_loan_simulator_seekbar_during) SeekBar mSeekBarDuring;

    @BindView(R.id.activity_loan_simulator_edittext_amount_bring) EditText mEditTextAmountBring;
    @BindView(R.id.activity_loan_simulator_edittext_cost_property) EditText mEditTextCostProperty;
    @BindView(R.id.activity_loan_simulator_seekbar_cost_property) SeekBar mSeekBarCostProperty;
    @BindView(R.id.activity_loan_simulator_seekbar_amount_bring) SeekBar mSeekBarAmountBring;
    @BindView(R.id.activity_loan_simulator_textview_cost_total_interest_and_insurance) TextView mTextViewCostTotalIinterestAndInsurance;

    @BindView(R.id.activity_loan_simulator_textview_cost_total_interest) TextView mTextViewCostTotalInterest;
    @BindView(R.id.activity_loan_simulator_textview_cost_total_insurance) TextView mTextViewCostTotalInsurance;

    String devise = "€";

    int costProperty = 10000;
    int amountBring = 0;
    int amountLoan = 0;
    int duringInMonth = 60;
    double interestRate = 2.96;
    double insuranceRate = 0.84;
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
        setContentView(R.layout.activity_loan_simulator);
        ButterKnife.bind(this);

        updateSimulator();

        mSeekBarAmount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                costProperty = mSeekBarAmount.getProgress() + amountBring;
                updateSimulator();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mEditTextAmount.setOnEditorActionListener((textView, i, keyEvent) -> {
            try {
                costProperty = Integer.parseInt(mEditTextAmount.getText().toString()
                        .replaceAll("\\s", "")
                        .replaceAll(devise, "")) + amountBring;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            updateSimulator();
            return false;
        });

        mSeekBarDuring.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                duringInMonth = mSeekBarDuring.getProgress();
                updateSimulator();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mSeekBarAmountBring.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                amountBring = mSeekBarAmountBring.getProgress();
                updateSimulator();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mEditTextAmountBring.setOnEditorActionListener((textView, i, keyEvent) -> {
            try {
                amountBring = Integer.parseInt(mEditTextAmountBring.getText().toString()
                        .replaceAll("\\s", "")
                        .replaceAll(devise, ""));
            } catch (Exception e) {
                amountBring = 0;
            }

            updateSimulator();
            return false;
        });

        mSeekBarCostProperty.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                costProperty = mSeekBarCostProperty.getProgress();
                updateSimulator();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mEditTextCostProperty.setOnEditorActionListener((textView, i, keyEvent) -> {
            try {
                costProperty = Integer.parseInt(mEditTextCostProperty.getText().toString()
                        .replaceAll("\\s", "")
                        .replaceAll(devise, ""));
            } catch (Exception e) {
                costProperty = 1000;
            }

            updateSimulator();

            return false;
        });

        mEditTextInterestRate.setOnEditorActionListener((textView, i, keyEvent) -> {
            try {
                interestRate = Double.parseDouble(mEditTextInterestRate.getText().toString()
                        .replaceAll("\\s","")
                        .replaceAll("%", ""));

            } catch (NumberFormatException e) {
                interestRate = 0.01;
            }

            updateSimulator();

            return false;
        });

        mEditTextInsuranceRate.setOnEditorActionListener((textView, i, keyEvent) -> {
            try {
                insuranceRate = Double.parseDouble(mEditTextInsuranceRate.getText().toString()
                        .replaceAll("\\s","")
                        .replaceAll("%", ""));
            } catch (NumberFormatException e) {
                insuranceRate = 0.01;
            }

            updateSimulator();

            return false;
        });

    }

    private void updateSimulator(){
        updateData();
        updateUI();
    }

    private void updateData(){
        amountLoan = costProperty - amountBring;

        monthlyPaymentInsurance = Utils.calculateMonthlyPaymentInsuranceOnly(amountLoan, insuranceRate / 100);
        monthlyPaymentInterest = Utils.calculateMonthlyPaymentInterestBankOnly(amountLoan, interestRate / 100, duringInMonth);
        monthlyPaymentTotal = (monthlyPaymentInsurance + monthlyPaymentInterest) + amountLoan / duringInMonth;

        costTotalInsurance = monthlyPaymentInsurance * (duringInMonth);
        costTotalInterest = monthlyPaymentInterest * (duringInMonth);
        costTotalInterestAndInsurance =  costTotalInterest + costTotalInsurance;
        costTotal = costTotalInterestAndInsurance + amountLoan;

    }

    private void updateUI(){
        NumberFormat nf = NumberFormat.getInstance();

        mEditTextInsuranceRate.setText(String.format("%s", insuranceRate + " %"));
        mEditTextInterestRate.setText(String.format("%s", interestRate + " %"));
        mTextViewMonthlyPaymentTotal.setText(String.format("%s %s/mois", nf.format(monthlyPaymentTotal), devise));

        mEditTextAmount.setText(String.format("%s %s", nf.format(amountLoan), devise));
        mTextViewDuring.setText(String.format("%d mois (%s ans)", duringInMonth, new DecimalFormat("#.#").format(duringInMonth / 12D)));
        mTextViewMonthlyPaymentInsurance.setText(String.format("%s %s/mois", nf.format(monthlyPaymentInsurance), devise));
        mTextViewMonthlyPaymentBank.setText(String.format("%s %s/mois", nf.format(monthlyPaymentInterest), devise));
        mEditTextAmountBring.setText(String.format("%s %s", nf.format(amountBring), devise));
        mEditTextCostProperty.setText(String.format("%s %s", nf.format(costProperty), devise));

        mTextViewCostTotalInterest.setText(String.format("%s %s", nf.format(costTotalInterest), devise));
        mTextViewCostTotalInsurance.setText(String.format("%s %s", nf.format(costTotalInsurance), devise));
        mTextViewCostTotalIinterestAndInsurance.setText(String.format("%s %s", nf.format(costTotalInterestAndInsurance), devise));
        mTextViewCostTotal.setText(String.format("%s %s", nf.format(costTotal), devise));

        mSeekBarCostProperty.setProgress(costProperty);
        mSeekBarAmountBring.setProgress(amountBring);
        mSeekBarAmount.setProgress(amountLoan);
        mSeekBarDuring.setProgress(this.duringInMonth);
    }

}