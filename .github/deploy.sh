#!/usr/bin/env bash

echo -e "Running deploy script...\n"
echo -e "Publishing artifacts...\n"
cd $HOME

rsync -r --quiet -e "ssh -p 7685 -o StrictHostKeyChecking=no" \
$HOME/work/xaero-minimap-paper/xaero-minimap-paper/build/repo/ \
u36810p330294@uskyblock.ovh:/repo.biemcraft.nl/public_html/repo/

echo -e "Publishing final plugin release...\n"
rsync -r --quiet -e "ssh -p 7685 -o StrictHostKeyChecking=no" \
$HOME/work/xaero-minimap-paper/xaero-minimap-paper/build/libs/xaero-minimap-paper-*.jar \
u36810p330294@uskyblock.ovh:domains/repo.biemcraft.nl/public_html/downloads/xaero-minimap-paper/
