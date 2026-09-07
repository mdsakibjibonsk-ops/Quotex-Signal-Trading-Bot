#!/bin/bash

# Quotex Signal Trading Bot - সম্পূর্ণ প্রজেক্ট সেটআপ স্ক্রিপ্ট

echo "================================================"
echo "Quotex Signal Trading Bot - সম্পূর্ণ সেটআপ"
echo "================================================"
echo ""

# পরীক্ষা করুন Java ইনস্টল আছে কিনা
if ! command -v java &> /dev/null; then
    echo "❌ Java 11 বা তার উপরে প্রয়োজন"
    echo "Java ডাউনলোড করুন: https://www.oracle.com/java/technologies/downloads/"
    exit 1
fi

echo "✅ Java পাওয়া গেছে"

# Gradle wrapper দিয়ে বিল্ড করুন
echo ""
echo "ধাপ ১: পুরানো বিল্ড পরিষ্কার করছি..."
./gradlew clean

echo ""
echo "ধাপ ২: রিলিজ APK তৈরি করছি..."
./gradlew assembleRelease

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ বিল্ড সম্পন্ন!"
    echo ""
    echo "APK লোকেশন:"
    echo "app/build/outputs/apk/release/app-release.apk"
    echo ""
    echo "ইনস্টল করতে:"
    echo "adb install app/build/outputs/apk/release/app-release.apk"
else
    echo ""
    echo "❌ বিল্ড ব্যর্থ হয়েছে"
    exit 1
fi
