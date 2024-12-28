#!/bin/bash
echo "Setting up Git hooks..."
cp -r .githooks/* .git/hooks/
chmod +x .git/hooks/pre-commit
echo "Git hooks setup complete."
