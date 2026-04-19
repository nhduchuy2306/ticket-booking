import { Alert, Button, Input, Progress, Tag } from "antd";
import React from "react";
import { FiArrowRight, FiClock, FiRefreshCw, FiShield, FiUsers } from "react-icons/fi";
import { useNavigate, useParams, useSearchParams } from "react-router-dom";
import { CustomerAuthStorage } from "../../services/CustomerAuth/CustomerAuthStorage.ts";
import { formatCountdown, hasValidWaitingRoomClearance, saveWaitingRoomClearance } from "../../utils/bookingSession.ts";
import "../auths/auth.scss";

const parsePositiveInteger = (value: string | null): number | null => {
    if (!value) {
        return null;
    }

    const parsedValue = Number(value);
    if (!Number.isFinite(parsedValue) || parsedValue <= 0) {
        return null;
    }

    return Math.floor(parsedValue);
};

const resolveDeadlineMs = (releaseAt: string | null, waitMinutes: number | null): number | null => {
    if (releaseAt) {
        const parsedReleaseAt = Date.parse(releaseAt);
        if (!Number.isNaN(parsedReleaseAt)) {
            return parsedReleaseAt;
        }
    }

    if (waitMinutes !== null) {
        return Date.now() + waitMinutes * 60 * 1000;
    }

    return null;
};

const createCaptchaChallenge = () => {
    const left = 2 + Math.floor(Math.random() * 8);
    const right = 1 + Math.floor(Math.random() * 8);

    if (Math.random() > 0.45) {
        return {question: `${left} + ${right}`, answer: left + right};
    }

    return {question: `${left + right} - ${right}`, answer: left};
};

const WaitingRoomPage: React.FC = () => {
    const navigate = useNavigate();
    const {id: eventId} = useParams<{ id?: string }>();
    const [searchParams] = useSearchParams();
    const eventName = searchParams.get("eventName")?.trim() || searchParams.get("title")?.trim() || (eventId ? `Event #${eventId}` : "your concert");
    const queuePosition = parsePositiveInteger(searchParams.get("position"));
    const queueTotal = parsePositiveInteger(searchParams.get("total"));
    const waitMinutes = parsePositiveInteger(searchParams.get("waitMinutes"));
    const releaseAt = searchParams.get("releaseAt");
    const nextPath = searchParams.get("next")?.trim() || (eventId ? `/gyp/events/${eventId}/choose-seats` : null);
    const customerName = CustomerAuthStorage.getCustomerName();
    const [deadlineMs] = React.useState<number | null>(() => resolveDeadlineMs(releaseAt, waitMinutes));
    const [secondsLeft, setSecondsLeft] = React.useState<number>(() => {
        if (deadlineMs === null) {
            return waitMinutes !== null ? waitMinutes * 60 : 0;
        }

        return Math.max(0, Math.floor((deadlineMs - Date.now()) / 1000));
    });
    const [captchaChallenge, setCaptchaChallenge] = React.useState(createCaptchaChallenge);
    const [captchaInput, setCaptchaInput] = React.useState("");
    const [captchaSolved, setCaptchaSolved] = React.useState<boolean>(() => hasValidWaitingRoomClearance(eventId));
    const [captchaMessage, setCaptchaMessage] = React.useState<string | null>(null);

    React.useEffect(() => {
        setCaptchaChallenge(createCaptchaChallenge());
        setCaptchaInput("");
        setCaptchaMessage(null);
        setCaptchaSolved(hasValidWaitingRoomClearance(eventId));

        if (deadlineMs === null) {
            setSecondsLeft(waitMinutes !== null ? waitMinutes * 60 : 0);
            return;
        }

        const updateCountdown = () => {
            setSecondsLeft(Math.max(0, Math.floor((deadlineMs - Date.now()) / 1000)));
        };

        updateCountdown();
        const timer = window.setInterval(updateCountdown, 1000);

        return () => window.clearInterval(timer);
    }, [deadlineMs, eventId, waitMinutes]);

    const isReady = deadlineMs === null || secondsLeft === 0;
    const canEnterSeatMap = isReady && captchaSolved;
    const progressPercent = queuePosition !== null && queueTotal !== null
            ? Math.max(0, Math.min(100, Math.round(((queueTotal - queuePosition + 1) / queueTotal) * 100)))
            : null;
    const queueLabel = queuePosition !== null
            ? queueTotal !== null
                    ? `${queuePosition.toLocaleString()} / ${queueTotal.toLocaleString()}`
                    : `#${queuePosition.toLocaleString()}`
            : "Calculating";
    const etaLabel = isReady
            ? (nextPath ? "Now" : "Ready")
            : formatCountdown(secondsLeft);

    const handleContinue = () => {
        if (nextPath && canEnterSeatMap) {
            navigate(nextPath);
            return;
        }

        navigate(eventId ? `/gyp/events/${eventId}` : "/gyp/");
    };

    const handleCaptchaSubmit = () => {
        const parsedAnswer = Number(captchaInput);
        if (!Number.isFinite(parsedAnswer)) {
            setCaptchaMessage("Please enter a valid number.");
            return;
        }

        if (parsedAnswer !== captchaChallenge.answer) {
            setCaptchaSolved(false);
            setCaptchaMessage("Captcha answer is incorrect. Please try again.");
            setCaptchaChallenge(createCaptchaChallenge());
            setCaptchaInput("");
            return;
        }

        setCaptchaSolved(true);
        setCaptchaMessage("Captcha verified. You can continue when your queue time is ready.");
        if (eventId) {
            saveWaitingRoomClearance(eventId, nextPath || undefined, deadlineMs || undefined);
        }
    };

    const handleRefresh = () => {
        window.location.reload();
    };

    const handleBackToEvent = () => {
        navigate(eventId ? `/gyp/events/${eventId}` : "/gyp/");
    };

    const handleHome = () => {
        navigate("/gyp/");
    };

    return (
            <div className="customer-auth-shell">
                <div className="customer-auth-orb customer-auth-orb-one"/>
                <div className="customer-auth-orb customer-auth-orb-two"/>

                <div className="customer-auth-grid">
                    <section className="customer-auth-hero">
                        <div>
                            <div className="customer-auth-brand">
                                <div className="customer-auth-brand-mark">GYP</div>
                                <div>
                                    <p className="customer-auth-kicker">Waiting room</p>
                                    <h1>{customerName ? `Hi ${customerName}, you are in line for ${eventName}` : `You are in line for ${eventName}`}</h1>
                                </div>
                            </div>

                            <p className="customer-auth-hero-copy">
                                Thousands of fans may try to buy at the same time. Keep this page open, solve the captcha,
                                and we will preserve your access path while the queue moves.
                            </p>
                        </div>

                        <div className="customer-auth-stat-grid">
                            <div className="customer-auth-stat-card">
                                <FiUsers/>
                                <span className="text-sm uppercase tracking-[0.16em] text-slate-400">Queue position</span>
                                <strong className="text-2xl text-white">{queueLabel}</strong>
                            </div>
                            <div className="customer-auth-stat-card">
                                <FiClock/>
                                <span className="text-sm uppercase tracking-[0.16em] text-slate-400">Estimated access</span>
                                <strong className="text-2xl text-white">{etaLabel}</strong>
                            </div>
                            <div className="customer-auth-stat-card">
                                <FiShield/>
                                <span className="text-sm uppercase tracking-[0.16em] text-slate-400">Session state</span>
                                <strong className="text-2xl text-white">{canEnterSeatMap ? "Ready to enter" : "Held securely"}</strong>
                            </div>
                        </div>

                        <div className="customer-auth-meta">
                            <span>Event access stays tied to this browser session.</span>
                            <span>{deadlineMs ? `Auto updates every second` : `Live queue information will appear when available`}</span>
                        </div>
                    </section>

                    <section className="customer-auth-card">
                        <div className="customer-auth-panel">
                            <div className="customer-auth-panel-heading flex items-start justify-between gap-4">
                                <div>
                                    <p className="customer-auth-kicker">Queue status</p>
                                    <h2>{canEnterSeatMap ? "Your turn is here" : "Please stay on this page"}</h2>
                                </div>
                                <Tag color={canEnterSeatMap ? "success" : "gold"} className="!m-0 !rounded-full !border-0 !px-3 !py-1 !font-semibold">
                                    {canEnterSeatMap ? "Ready" : "Waiting"}
                                </Tag>
                            </div>

                            <div className="mt-6 rounded-2xl border border-white/10 bg-slate-950/55 p-5">
                                <div className="flex items-end justify-between gap-4">
                                    <div>
                                        <p className="text-xs font-bold uppercase tracking-[0.22em] text-slate-400">Spot in line</p>
                                        <div className="mt-1 text-5xl font-black text-white">{queuePosition !== null ? queuePosition.toLocaleString() : "—"}</div>
                                    </div>
                                    <div className="text-right">
                                        <p className="text-xs font-bold uppercase tracking-[0.22em] text-slate-400">Time left</p>
                                        <div className="mt-1 text-3xl font-black text-amber-400">{isReady ? "00:00" : formatCountdown(secondsLeft)}</div>
                                    </div>
                                </div>

                                {progressPercent !== null ? (
                                        <Progress
                                                className="mt-5 [&_.ant-progress-outer]:!mr-0 [&_.ant-progress-bg]:!bg-[linear-gradient(90deg,#fbbf24_0%,#38bdf8_100%)]"
                                                percent={progressPercent}
                                                strokeLinecap="round"
                                                showInfo={false}
                                                trailColor="rgba(148, 163, 184, 0.16)"
                                        />
                                ) : (
                                        <div className="mt-5 h-2 overflow-hidden rounded-full bg-slate-800/80">
                                            <div className="h-full w-1/3 animate-pulse rounded-full bg-gradient-to-r from-amber-400 via-yellow-300 to-sky-400"/>
                                        </div>
                                )}

                                <p className="mt-4 text-sm leading-6 text-slate-300">
                                    {isReady
                                            ? "Your access window is ready. Solve the captcha to continue to seat selection."
                                            : "Keep this tab active. We will update the timer automatically and keep your booking context ready for the next step."}
                                </p>
                            </div>

                            <div className="mt-5 rounded-2xl border border-white/10 bg-slate-950/55 p-5">
                                <div className="flex items-center justify-between gap-3">
                                    <div>
                                        <p className="text-xs font-bold uppercase tracking-[0.22em] text-slate-400">Basic captcha</p>
                                        <p className="mt-1 text-sm text-slate-200">Solve this simple math check before entering seat selection.</p>
                                    </div>
                                    <Tag color={captchaSolved ? "success" : "gold"} className="!m-0 !rounded-full !border-0 !px-3 !py-1 !font-semibold">
                                        {captchaSolved ? "Verified" : "Required"}
                                    </Tag>
                                </div>

                                <div className="mt-4 flex flex-col gap-3 sm:flex-row">
                                    <div className="flex min-h-12 flex-1 items-center rounded-xl border border-slate-600/30 bg-slate-900/92 px-4 text-lg font-semibold text-white">
                                        {captchaChallenge.question} = ?
                                    </div>
                                    <Input
                                            value={captchaInput}
                                            onChange={(e) => setCaptchaInput(e.target.value)}
                                            onPressEnter={handleCaptchaSubmit}
                                            placeholder="Your answer"
                                            inputMode="numeric"
                                            className="h-12 rounded-xl border-slate-600/30 bg-slate-900/92 text-slate-50 placeholder:text-slate-500"
                                    />
                                    <Button
                                            type="primary"
                                            className="h-12 rounded-xl border-0 bg-gradient-to-r from-sky-400 to-cyan-500 font-bold text-slate-950"
                                            onClick={handleCaptchaSubmit}
                                    >
                                        Verify
                                    </Button>
                                </div>

                                {captchaMessage && (
                                        <Alert
                                                className="mt-4 rounded-xl border border-white/10 bg-slate-900/85 text-slate-50 shadow-lg [&_.ant-alert-message]:text-slate-50 [&_.ant-alert-description]:text-slate-200"
                                                type={captchaSolved ? "success" : "error"}
                                                message={captchaSolved ? "Captcha passed" : "Captcha notice"}
                                                description={captchaMessage}
                                                showIcon
                                        />
                                )}
                            </div>

                            <div className="mt-5 grid gap-3">
                                <Button
                                        type="primary"
                                        htmlType="button"
                                        className="h-12 rounded-xl border-0 bg-gradient-to-r from-amber-400 to-amber-500 font-extrabold tracking-wide text-slate-950 shadow-lg shadow-amber-500/20 hover:from-amber-300 hover:to-amber-500 hover:text-slate-950"
                                        onClick={handleContinue}
                                        disabled={!canEnterSeatMap}
                                        icon={<FiArrowRight/>}
                                        block
                                >
                                    {!captchaSolved
                                            ? "Verify captcha first"
                                            : !isReady
                                                    ? `Available in ${formatCountdown(secondsLeft)}`
                                                    : (nextPath ? "Continue to ticket selection" : "Back to event")}
                                </Button>

                                <Button htmlType="button" className="customer-google-button" onClick={handleRefresh} icon={<FiRefreshCw/>} block>
                                    Refresh status
                                </Button>

                                <Button
                                        htmlType="button"
                                        className="h-12 rounded-xl border border-slate-600/30 bg-slate-900/92 font-semibold text-slate-100 hover:border-amber-400/40 hover:text-white"
                                        onClick={handleBackToEvent}
                                        block
                                >
                                    Back to event
                                </Button>

                                <Button
                                        htmlType="button"
                                        className="h-12 rounded-xl border border-slate-600/20 bg-transparent font-semibold text-slate-200 hover:border-slate-400/40 hover:text-white"
                                        onClick={handleHome}
                                        block
                                >
                                    Browse all events
                                </Button>
                            </div>

                            <div className="customer-auth-meta mt-5 rounded-2xl border border-white/10 bg-slate-950/45 px-4 py-3 text-sm text-slate-300/85">
                                <span>When your slot opens and captcha is verified, you can continue without logging in again.</span>
                                <span>{eventId ? `Event ID: ${eventId}` : "Queue access is shown in generic mode"}</span>
                            </div>
                        </div>
                    </section>
                </div>
            </div>
    );
};

export default WaitingRoomPage;

