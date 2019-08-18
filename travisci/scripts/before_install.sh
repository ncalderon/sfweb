#!/usr/bin/env bash
openssl aes-256-cbc -K $encrypted_23a5828dec86_key -iv $encrypted_23a5828dec86_iv -in secrets.tar.enc -out secrets.tar -d
eval "$(ssh-agent -s)"
chmod +x secrets.tar
tar xvf secrets.tar
chmod u+xr,o-rwx ./travisci/.ssh
chmod u+xr,o-rwx ./travisci/.ssh/id_rsa
chmod u+xr,o-rwx ./travisci/.ssh/id_rsa.pub
chmod u+xr,o-rwx ./travisci/.ssh/config
chmod u+xr,o-rwx ./travisci/.ssh/sshpass
mv -f ./travisci/.ssh/id_rsa ~/.ssh/id_rsa
mv -f ./travisci/.ssh/id_rsa.pub ~/.ssh/id_rsa.pub
mv -f ./travisci/.ssh/config ~/.ssh/config
mv -f ./travisci/.ssh/sshpass ~/.ssh/sshpass
chmod u+xr,o-rwx,g-rwx ~/.ssh
chmod u+xr,o-rwx,g-rwx ~/.ssh/id_rsa
chmod u+xr,o-rwx,g-rwx ~/.ssh/id_rsa.pub
chmod u+xr,o-rwx,g-rwx ~/.ssh/config
chmod u+xr,o-rwx,g-rwx ~/.ssh/sshpass
ssh-add ~/.ssh/id_rsa
exit 0
