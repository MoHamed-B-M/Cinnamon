# Changelog

All notable changes to **Cinnamon** are documented here. This project follows [Keep a Changelog](https://keepachangelog.com/) and uses `beta` for preview and `main` for stable releases.

## [1.0.5] - 2026-09-02 — Beta `c6d4e93`..`bbf1215` + `58cc40f`..`f44fd04`

### Added
- **Material 3 Expressive Call UI** `CallScreen.kt`, `CallBottomBar.kt`, `IncomingBottomBar.kt`, `Dialpad.kt`, `AudioSwitcher.kt`
  - Tonal surfaces `surfaceContainer*`, `primaryContainer`, `error`, `secondaryContainer`, `RoundedCornerShape(20-50)` / `Cookie9Sided` squircle, `MotionScheme.expressive()` + `bouncySpec` springs, 8dp spacing, `AssistChip` status chips.
- **Call bubble overlay** `CallBubble.kt` `CallOverlayManager.kt` — system `TYPE_APPLICATION_OVERLAY` pill `28dp` with tonal blur, avatar pulsing dot, `Mute`/`End` actions, tap to expand to `CallActivity`. Managed by `CallService` via `CinnamonApplication.isCallActivityVisibleFlow`.
- **M3 Expressive blur** — `Navigation.kt` `hazeSource` + `ScreenSelection.kt` `cuteHazeEffect(surfaceContainer 0.78f)`, `CallScreen.kt` `hazeSource` + bottomBar `cuteHazeEffect(surfaceContainer 0.82f)`. Toggleable via `Look & Feel`.
- **Permissions screen** `SettingsPermissions.kt` — `Permissions` category `phone_filled` with `Default dialer` `primaryContainer` and `Default messaging` `tertiaryContainer` cards, `Button` `shapes()` to `RoleManager`/`TelecomManager`, status `✓`.
- **Look & Feel blur toggles** `SettingsLookAndFeel.kt` — `Expressive blur (entire app)` `Blur under navigation bar` `Motion blur` `Incoming call full-screen popup` via `DataStore` `ENABLE_EXPRESSIVE_BLUR`, `ENABLE_MOTION_BLUR`, `INCOMING_CALL_FULLSCREEN`.
- **Full dialer manifest** `AndroidManifest.xml` — added `VIEW tel:` `BROWSABLE` to `MainActivity` and `CallActivity` for `tel:` links, `SYSTEM_ALERT_WINDOW` for bubble.

### Changed
- **Incoming bottom bar** — removed swipe `Animatable`/`draggable` pill, kept only `Answer` `primary` / `Decline` `error` `64dp` `RoundedCornerShape(50)` buttons with haptics.
- **Setup** `SetupScreen.kt:80` — removed dialer step, only `SetupDefaultMessageApp` → `onGotoApp`; `MainActivity.kt:32` now only checks `ROLE_SMS` (`Telephony.Sms`), dialer no longer gates entry.
- **Calling** `CallingViewModel.kt:50` — removed `defaultDialerPackage` gate; always `startCall()` + optimistic `DIALING` + `startActivity(CallActivity)` so call button always opens Cinnamon UI. Incoming handled via `CallService` `fullScreenIntent` / bubble.
- **Look & Feel** — moved `Incoming call popup` from `Permissions` to `Look & Feel` (per request) with `Switch`.

### Fixed
- **CallScreen compile** `ColumnScope.AnimatedVisibility` inside `BoxScope` → `androidx.compose.animation.AnimatedVisibility` + `ToggleButtonDefaults.shapes` unresolved + `R.string.mic_off` missing (`CallBottomBar.kt:158`), Haze provider brace mismatch (`CallScreen.kt:464`).
- **End call not working** `CallManager.kt:84` `CallService.kt:301` — fallback `updateCallState(ENDED)` when `androidCallCallback==null`/`cuteCall==null` (optimistic UI before `onCallAdded`), `calls.firstOrNull()?.disconnect()` for multi-call.
- **SIM chooser opening system dialer** `CallManager.kt:64` — `savedHandle ?: callCapablePhoneAccounts.firstOrNull() ?: getDefaultOutgoingPhoneAccount` fallback + `CallService:224` `STATE_SELECT_PHONE_ACCOUNT` auto-pick first handle, keeping chooser inside Cinnamon.
- **Phone settings crash** `SimsRepository.kt:38` `SettingsPhone.kt:35` — `getPhoneAccount` null + `SecurityException` try-catch, empty handles placeholder `"No SIM available or permission needed"`.
- **Navigation only showing Messages** `ScreenSelection.kt:61` — fixed `by rememberExpressiveBlurEnabled()` delegate (was `getValue(...)` function) and `Surface` `Color.Transparent` fallback; restored `ShortNavigationBar` visibility.
- **Default dialer not working** `SettingsPermissions.kt:207` — `RoleManager` intent launched without `NEW_TASK` via `ActivityResultLauncher`, fallback `TelecomManager.ACTION_CHANGE_DEFAULT_DIALER` with `EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME`.
- **Incoming popup option** — added `DataStore` `INCOMING_CALL_FULLSCREEN` (default `true`), `CallService:186` `createIncomingNotification(..., useFullScreen)` + conditional `launchCallActivity()` (bubble shows when disabled).

### Services — Full Dialer
- `CallService.kt` — `onBringToForeground`, `STATE_SELECT_PHONE_ACCOUNT` handling, foreground-first notification, `CuteCallScreeningService.kt` blocked-number check via `BlockedNumberContract`, `onCallRemoved` cleanup, `onCallAudioStateChanged` routes.

### Build & Release
- **Beta preview** `release_beta.yml` — `push: beta` ephemeral `beta` tag (`gh release delete beta --cleanup-tag`), `Cinnamon_<ver>-beta+<sha>.apk`, `prerelease:true` `make_latest:false`, 7-day artifact, `Haze` `1.7.2` `gradle/actions/setup-gradle@v4`, JDK 21, robust `KEYSTORE_FILE_B64` decode (`sed`/`tr`/`base64 -d`) + `keytool -list` + `importkeystore` `KEY_PASSWORD` validation.
- **Stable** `release_stable.yml` — `push: main` `v<ver>` `generate_release_notes` `make_latest:true`, tag existence check, same signing, `gradle/actions/setup-gradle`.
- **Keystore** `scripts/generate-keystore.ps1/.sh` `+ .github/KEYSTORE_SETUP.md` — `KEYSTORE_FILE_B64`, `KEYSTORE_PASSWORD`, `KEY_ALIAS`, `KEY_PASSWORD` secrets, `CinnamonApplication` notification channels `calls_id` `INCOMING_MESSAGES_CHANNEL_ID`.

---

## [1.0.4] - 2026-08-26
- Voicemail, call log lookups refined `c646c7e`.

## [1.0.3] - 2026-08-22
- Call logs and settings navigation refactor `6f0f655`.
