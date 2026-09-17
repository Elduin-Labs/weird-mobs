# For a grown-up: getting Weird Mobs onto Modrinth

Elduin has finished a mod and wants it on Modrinth so people can install it from
the Modrinth app. Everything is done and tested except one step, and that step
needs an account password — which is why it needs you.

**It takes about a minute.** You only ever have to do this once for this mod.
Every release after this one is automatic.

---

## Why a grown-up

Making a **brand new project page** on Modrinth requires being signed in to the
account. Claude isn't allowed to type passwords or sign in to accounts, by
design. The automatic publishing that happens afterwards uses a key that's
already stored safely on GitHub — that part needs nobody.

---

## Option A — make the page yourself (easiest)

1. Go to **modrinth.com** and sign in as **ItsElduin**.
2. Click the profile picture, top right → **Creator Dashboard**.
3. Open **Projects** → **Create a project**.
4. Fill in exactly this:

   | Field | What to put |
   |---|---|
   | Name | `Weird Mobs` |
   | URL / slug | `weird-mobs` |
   | Summary | A mod full of weird mobs. The first one is the OKIE - a robot that teaches you how to spell. |
   | Project type | Mod |
   | License | MIT |

5. Press create.
6. Look at the address bar, or the project's Settings page, and copy the
   **project ID** (a short code like `AbCdEfGh`).
7. Tell Claude that code.

Claude then does the rest on its own: uploads the icon, writes the description,
builds and attaches both jars (Minecraft 1.21.8 and 1.21.4), marks Fabric API as
required, and submits the project for review.

You don't need to upload any files yourself.

---

## Option B — let Claude do all of it

If you'd rather not fill the form in:

1. Go to **modrinth.com/settings/pats** while signed in as ItsElduin.
2. Create a Personal Access Token with the **create projects** permission.
   Give it a short expiry — a day is plenty.
3. Open `~/GitHub/weird-mobs/.env` and replace this line:

   ```
   PUB_MODRINTH_TOKEN=your_modrinth_token_here
   ```

   with the token. That file is git-ignored, so it never leaves this computer.
4. Tell Claude it's there.
5. **Revoke the token** on the same settings page once it's done.

This is exactly what was done for `pearl-cannon`, `organs` and `ring-bong-os`
back in August, and that token was revoked straight afterwards.

---

## What gets published

- **Weird Mobs 1.0.0**, MIT licensed, source at
  <https://github.com/Elduin-Labs/weird-mobs>
- One mob, the OKIE: a friendly robot that wanders, blinks, and spells words out
  for you when you right-click it. It cannot attack.
- Built for Minecraft **1.21.8** and **1.21.4**, Fabric, needs Fabric API.

Nothing on the page names Elduin beyond his first name. No surname, no school,
no town, no email, no photographs.

New projects always go into Modrinth's human review queue before they appear
publicly. That's normal and nothing to do with the mod — approval is up to their
moderators and usually takes a day or two.

---

## One unrelated thing, while you're here

`git`, `python3` and `curl` on this Mac currently refuse to run until the Xcode
licence is accepted. There's a workaround in place so nothing is blocked, but the
real fix is one command in Terminal, and it needs your password:

```bash
sudo xcodebuild -license accept
```
