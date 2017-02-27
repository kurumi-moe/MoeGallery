#!/bin/bash
 
# This script generates environment variables for pull requests and forks.

export GIT_TAG=$(git describe --abbrev=0 --tags)

mkdir "$ANDROID_SDK/licenses" || true
echo -e "\n8933bad161af4178b1185d1a37fbf41ea5269c55" > "$ANDROID_SDK/licenses/android-sdk-license"
echo -e "\n84831b9409646a918e30573bab4c9c91346d8abd" > "$ANDROID_SDK/licenses/android-sdk-preview-license"

if [ -z "$encrypted_d6b6b9994385_key" ] ; then
    # It's running from pull requests or forks, set vars.
     
    TEXT="I_AM_PUBLIC_AND_NOT_USED_FOR_RELEASE"
     
    # encrypted key and iv is taking from 'openssl enc -nosalt -aes-256-cbc -pass pass:I_AM_PUBLIC_AND_NOT_USED_FOR_RELEASE -P'
     
    export encrypted_d6b6b9994385_key="12CF1B5E0D192628AA922230549EEDFD889E6CF7463933C6DABD9A1300FCA23D"
    export encrypted_d6b6b9994385_iv="66813CF28D04CD129D57436B78DECBA4"
     
    export GITHUB_TOKEN="$TEXT"
    export KEYSTORE_PASSWORD="$TEXT"
    export ALIAS_PASSWORD="$TEXT"
    export ALIAS="$TEXT"
    export API_KEY="$TEXT"
    export JUHE_API_KEY="$TEXT"
    export LEANCLOUD_APP_ID="$TEXT"
    export LEANCLOUD_APP_KEY="$TEXT"
     
    # Overlay release.jks.enc
     
    # Travis-ci is using 'openssl aes-256-cbc -K 12CF1B5E0D192628AA922230549EEDFD889E6CF7463933C6DABD9A1300FCA23D -iv 66813CF28D04CD129D57436B78DECBA4 -in public.jks.enc -out public.jks -d' to decrypt the file.
    mv "public.jks.enc" "release.jks.enc"
fi
