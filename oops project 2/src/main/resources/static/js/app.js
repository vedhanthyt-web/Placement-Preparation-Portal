/**
 * Placement Preparation Portal - Frontend Application Core
 * Connects UI to Spring Boot OOP Backend APIs
 */

const app = {
    // Current application state
    currentUser: null,
    activeTab: 'auth',
    activeTest: null,
    testTimerSeconds: 0,
    timerInterval: null,
    studentAnswers: {}, // { questionId: selectedOption }
    cachedPracticeQuestions: [],
    currentPracticeCategory: 'ALL',

    /**
     * Initialization upon DOM Ready
     */
    init() {
        console.log("Placement Portal initialized.");
        // Restore session from localStorage if available
        const saved = localStorage.getItem('placement_portal_user');
        if (saved) {
            try {
                this.currentUser = JSON.parse(saved);
                this.updateAuthUI();
                this.showTab('dashboard');
            } catch (e) {
                localStorage.removeItem('placement_portal_user');
            }
        } else {
            this.showTab('auth');
        }
    },

    /**
     * Navigation & Tab Switching
     */
    showTab(tabId) {
        // If not logged in and trying to access protected tabs, redirect to auth
        if (!this.currentUser && tabId !== 'auth' && tabId !== 'oopguide') {
            this.showAlert('Please sign in or register to access the portal features.', 'info');
            tabId = 'auth';
        }

        // Admin-only guard
        if (tabId === 'admin' && (!this.currentUser || this.currentUser.role !== 'ADMIN')) {
            this.showAlert('Access denied: Admin privileges required.', 'danger');
            tabId = 'dashboard';
        }

        this.activeTab = tabId;

        // Hide all tabs
        document.querySelectorAll('.tab-pane').forEach(el => el.classList.add('hidden'));

        // Show active tab
        const activePane = document.getElementById(`tab-${tabId}`);
        if (activePane) {
            activePane.classList.remove('hidden');
        }

        // Update nav buttons
        document.querySelectorAll('.nav-btn').forEach(btn => {
            if (btn.getAttribute('data-tab') === tabId) {
                btn.classList.add('active');
            } else {
                btn.classList.remove('active');
            }
        });

        // Trigger tab-specific loaders
        if (tabId === 'dashboard') this.loadDashboard();
        if (tabId === 'practice') this.loadPracticeQuestions(this.currentPracticeCategory);
        if (tabId === 'results') this.loadResultsHistory();
        if (tabId === 'eligibility') this.loadEligibilityTracker();
        if (tabId === 'admin') this.loadAdminPanel();
    },

    /**
     * Authentication Logic
     */
    async handleLogin(event) {
        event.preventDefault();
        const email = document.getElementById('login-email').value.trim();
        const password = document.getElementById('login-password').value;

        try {
            const res = await fetch('/api/auth/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email, password })
            });

            if (!res.ok) {
                const err = await res.json();
                throw new Error(err.message || 'Login failed');
            }

            const user = await res.json();
            this.setUserSession(user);
            this.showAlert(`Welcome back, ${user.name}!`, 'success');
            this.showTab(user.role === 'ADMIN' ? 'admin' : 'dashboard');
        } catch (error) {
            this.showAlert(error.message, 'danger');
        }
    },

    async quickLogin(email, password) {
        document.getElementById('login-email').value = email;
        document.getElementById('login-password').value = password;
        const fakeEvent = { preventDefault: () => {} };
        await this.handleLogin(fakeEvent);
    },

    async handleRegister(event) {
        event.preventDefault();
        const studentPayload = {
            name: document.getElementById('reg-name').value.trim(),
            rollNumber: document.getElementById('reg-roll').value.trim(),
            email: document.getElementById('reg-email').value.trim(),
            department: document.getElementById('reg-dept').value,
            cgpa: parseFloat(document.getElementById('reg-cgpa').value),
            backlogs: parseInt(document.getElementById('reg-backlogs').value, 10),
            password: document.getElementById('reg-pass').value
        };

        try {
            const res = await fetch('/api/auth/register', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(studentPayload)
            });

            if (!res.ok) {
                const err = await res.json();
                throw new Error(err.message || 'Registration failed');
            }

            const student = await res.json();
            this.showAlert('Registration successful! Please sign in with your credentials.', 'success');
            document.getElementById('login-email').value = student.email;
            document.getElementById('login-password').value = '';
            document.getElementById('login-password').focus();
        } catch (error) {
            this.showAlert(error.message, 'danger');
        }
    },

    setUserSession(user) {
        this.currentUser = user;
        localStorage.setItem('placement_portal_user', JSON.stringify(user));
        this.updateAuthUI();
    },

    logout() {
        this.currentUser = null;
        localStorage.removeItem('placement_portal_user');
        this.updateAuthUI();
        this.showAlert('You have been logged out.', 'info');
        this.showTab('auth');
    },

    updateAuthUI() {
        const navLinks = document.getElementById('nav-links');
        const userBadge = document.getElementById('user-badge');
        const loginPrompt = document.getElementById('login-nav-prompt');
        const adminBtn = document.getElementById('nav-admin-btn');

        if (this.currentUser) {
            navLinks.classList.remove('hidden');
            userBadge.classList.remove('hidden');
            loginPrompt.classList.add('hidden');

            document.getElementById('user-name-display').textContent = this.currentUser.name;
            document.getElementById('user-role-pill').textContent = this.currentUser.role;

            if (this.currentUser.role === 'ADMIN') {
                adminBtn.classList.remove('hidden');
            } else {
                adminBtn.classList.add('hidden');
            }
        } else {
            navLinks.classList.add('hidden');
            userBadge.classList.add('hidden');
            loginPrompt.classList.remove('hidden');
        }
    },

    async refreshCurrentStudent() {
        if (!this.currentUser || this.currentUser.role !== 'STUDENT') return;
        try {
            const res = await fetch(`/api/auth/student/${this.currentUser.id}`);
            if (res.ok) {
                const updated = await res.json();
                this.currentUser = updated;
                localStorage.setItem('placement_portal_user', JSON.stringify(updated));
            }
        } catch (e) {
            console.error("Error refreshing student data:", e);
        }
    },

    /**
     * Dashboard Data Loader
     */
    async loadDashboard() {
        await this.refreshCurrentStudent();

        if (this.currentUser.role === 'STUDENT') {
            document.getElementById('dash-greeting').textContent = `Welcome back, ${this.currentUser.name}! Track your readiness and crack campus recruitments.`;
            document.getElementById('dash-student-name').textContent = this.currentUser.name;
            document.getElementById('dash-student-meta').textContent = `Roll: ${this.currentUser.rollNumber} | ${this.currentUser.department}`;
            document.getElementById('dash-cgpa-tag').textContent = `CGPA: ${this.currentUser.cgpa.toFixed(2)}`;
            document.getElementById('dash-backlogs-tag').textContent = `Backlogs: ${this.currentUser.backlogs}`;
            document.getElementById('dash-role-tag').textContent = `Candidate Profile`;

            document.getElementById('dash-tests-count').textContent = this.currentUser.testsAttempted || 0;
            document.getElementById('dash-avg-score').textContent = `${(this.currentUser.averageScore || 0).toFixed(1)}%`;

            // Compute readiness score
            const avg = this.currentUser.averageScore || 0;
            const cgpaScore = (this.currentUser.cgpa / 10.0) * 100;
            const backlogPenalty = this.currentUser.backlogs * 15;
            let readiness = Math.round((cgpaScore * 0.5) + (avg * 0.5) - backlogPenalty);
            if (readiness < 0) readiness = 0;
            if (readiness > 100) readiness = 100;

            document.getElementById('dash-readiness-pct').textContent = `${readiness}%`;
            let readinessLabel = 'Needs Assessment Practice';
            if (readiness >= 80) readinessLabel = 'Ready for Tier-1 Super Dream Drives';
            else if (readiness >= 65) readinessLabel = 'Ready for Core & IT Service Drives';
            else if (readiness >= 50) readinessLabel = 'Average - Improve Test Score';
            document.getElementById('dash-readiness-label').textContent = readinessLabel;

            // Load results summary for highest score
            try {
                const sumRes = await fetch(`/api/results/student/${this.currentUser.id}/summary`);
                if (sumRes.ok) {
                    const sum = await sumRes.json();
                    document.getElementById('dash-highest-score').textContent = `${(sum.highestPercentage || 0).toFixed(1)}%`;
                }
            } catch (e) {}

            // Load drives and calculate eligible count
            try {
                const eligRes = await fetch(`/api/drives/eligibility/${this.currentUser.id}`);
                if (eligRes.ok) {
                    const report = await eligRes.json();
                    const eligibleCount = report.filter(d => d.isEligible).length;
                    document.getElementById('dash-eligible-count').textContent = eligibleCount;

                    // Render snapshot
                    const container = document.getElementById('dash-drives-list');
                    container.innerHTML = '';
                    report.slice(0, 3).forEach(drive => {
                        const div = document.createElement('div');
                        div.className = 'drive-snap-item';
                        div.innerHTML = `
                            <div class="drive-snap-info">
                                <h4>${drive.companyName} — ${drive.jobRole}</h4>
                                <span>₹ ${drive.packageLpa} LPA | Min CGPA: ${drive.minCgpa}</span>
                            </div>
                            <span class="badge-pill ${drive.isEligible ? 'bg-success' : 'bg-danger'}">
                                ${drive.isEligible ? 'Eligible ✅' : 'Criteria Pending'}
                            </span>
                        `;
                        container.appendChild(div);
                    });
                }
            } catch (e) {}

        } else if (this.currentUser.role === 'ADMIN') {
            document.getElementById('dash-greeting').textContent = `Admin Mode: Logged in as ${this.currentUser.name} (${this.currentUser.employeeId})`;
            document.getElementById('dash-student-name').textContent = this.currentUser.name;
            document.getElementById('dash-student-meta').textContent = `Employee ID: ${this.currentUser.employeeId} | ${this.currentUser.departmentManaged}`;
            document.getElementById('dash-cgpa-tag').textContent = `Role: Administrator`;
            document.getElementById('dash-backlogs-tag').textContent = `Status: Active`;
            document.getElementById('dash-role-tag').textContent = `Training & Placement Officer`;
            document.getElementById('dash-readiness-pct').textContent = `ADMIN`;
            document.getElementById('dash-readiness-label').textContent = `System Overseer`;
        }
    },

    /**
     * Practice Zone Logic
     */
    startPracticeWithCategory(cat) {
        this.currentPracticeCategory = cat;
        this.showTab('practice');
    },

    async loadPracticeQuestions(category) {
        this.currentPracticeCategory = category;

        // Update active filter button
        document.querySelectorAll('.filter-btn').forEach(btn => {
            if (btn.getAttribute('data-cat') === category) {
                btn.classList.add('active');
            } else {
                btn.classList.remove('active');
            }
        });

        const container = document.getElementById('practice-questions-container');
        container.innerHTML = '<div class="text-center text-muted" style="padding: 2rem;">Loading questions...</div>';

        try {
            const res = await fetch(`/api/questions/practice?category=${category}`);
            if (!res.ok) throw new Error('Failed to load questions');
            const questions = await res.json();
            this.cachedPracticeQuestions = questions;

            if (questions.length === 0) {
                container.innerHTML = '<div class="card text-center text-muted">No questions available in this category yet.</div>';
                return;
            }

            container.innerHTML = '';
            questions.forEach((q, idx) => {
                const card = document.createElement('div');
                card.className = 'question-card';
                card.id = `practice-q-${q.id}`;
                card.innerHTML = `
                    <div class="question-meta">
                        <div>
                            <span class="pill-cat">${q.category}</span>
                            <span class="text-muted" style="margin-left: 8px; font-size: 0.85rem;">Q${idx + 1}</span>
                        </div>
                        <div>
                            <span class="pill-diff">${q.difficulty || 'MEDIUM'}</span>
                            <span class="text-muted" style="margin-left: 8px; font-size: 0.85rem;">Marks: ${q.marks || 1}</span>
                        </div>
                    </div>
                    <div class="question-text">${this.escapeHtml(q.questionText)}</div>
                    <div class="options-grid">
                        <div class="option-box" data-opt="A" onclick="app.selectPracticeOption(${q.id}, 'A')">
                            <span class="opt-letter">A</span>
                            <span>${this.escapeHtml(q.optionA)}</span>
                        </div>
                        <div class="option-box" data-opt="B" onclick="app.selectPracticeOption(${q.id}, 'B')">
                            <span class="opt-letter">B</span>
                            <span>${this.escapeHtml(q.optionB)}</span>
                        </div>
                        <div class="option-box" data-opt="C" onclick="app.selectPracticeOption(${q.id}, 'C')">
                            <span class="opt-letter">C</span>
                            <span>${this.escapeHtml(q.optionC)}</span>
                        </div>
                        <div class="option-box" data-opt="D" onclick="app.selectPracticeOption(${q.id}, 'D')">
                            <span class="opt-letter">D</span>
                            <span>${this.escapeHtml(q.optionD)}</span>
                        </div>
                    </div>
                    <div id="practice-feedback-${q.id}" class="hidden"></div>
                `;
                container.appendChild(card);
            });
        } catch (error) {
            container.innerHTML = `<div class="card text-center text-muted">Error loading practice questions: ${error.message}</div>`;
        }
    },

    async selectPracticeOption(questionId, optionLetter) {
        const card = document.getElementById(`practice-q-${questionId}`);
        if (!card) return;

        // Reset previous selections in this card
        const optionBoxes = card.querySelectorAll('.option-box');
        optionBoxes.forEach(box => {
            box.classList.remove('selected', 'opt-correct', 'opt-wrong');
            if (box.getAttribute('data-opt') === optionLetter) {
                box.classList.add('selected');
            }
        });

        // Instant evaluate via backend API
        try {
            const res = await fetch('/api/questions/practice/evaluate', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ questionId, selectedOption: optionLetter })
            });

            if (!res.ok) throw new Error('Evaluation error');
            const evalResult = await res.json();

            // Highlight correct and wrong options
            optionBoxes.forEach(box => {
                const opt = box.getAttribute('data-opt');
                if (opt === evalResult.correctOption) {
                    box.classList.add('opt-correct');
                } else if (opt === evalResult.selectedOption && !evalResult.isCorrect) {
                    box.classList.add('opt-wrong');
                }
            });

            // Display feedback box
            const feedbackBox = document.getElementById(`practice-feedback-${questionId}`);
            feedbackBox.className = 'explanation-box';
            feedbackBox.innerHTML = `
                <div style="font-weight: 700; margin-bottom: 4px; color: ${evalResult.isCorrect ? 'var(--success)' : 'var(--danger)'}">
                    ${evalResult.isCorrect ? '✅ Correct Answer! (+1 Mark)' : `❌ Incorrect. The correct answer is Option ${evalResult.correctOption}.`}
                </div>
                <div><strong>Explanation:</strong> ${this.escapeHtml(evalResult.explanation || 'No detailed explanation provided.')}</div>
            `;
            feedbackBox.classList.remove('hidden');

        } catch (error) {
            console.error('Error evaluating practice answer:', error);
        }
    },

    /**
     * Timed Mock Test Engine
     */
    async startMockTest() {
        if (!this.currentUser) {
            this.showAlert('Please sign in to take a mock test.', 'danger');
            this.showTab('auth');
            return;
        }

        const category = document.getElementById('mock-category-select').value;
        const count = parseInt(document.getElementById('mock-count-select').value, 10);
        const durationMinutes = parseInt(document.getElementById('mock-duration-select').value, 10);

        try {
            const res = await fetch('/api/mocktest/start', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    studentId: this.currentUser.id,
                    category,
                    count,
                    durationMinutes
                })
            });

            if (!res.ok) {
                const err = await res.json();
                throw new Error(err.message || 'Failed to start mock test');
            }

            const mockTest = await res.json();
            this.activeTest = mockTest;
            this.studentAnswers = {};

            // Switch to test running UI
            document.getElementById('mock-config-panel').classList.add('hidden');
            document.getElementById('mock-result-panel').classList.add('hidden');
            document.getElementById('mock-running-panel').classList.remove('hidden');

            document.getElementById('active-test-title').textContent = mockTest.title;
            document.getElementById('active-test-meta').textContent = `${mockTest.questions.length} Questions | Duration: ${mockTest.durationMinutes} Minutes`;

            // Render questions in test
            const container = document.getElementById('mock-questions-list');
            container.innerHTML = '';
            mockTest.questions.forEach((q, idx) => {
                const qDiv = document.createElement('div');
                qDiv.className = 'question-card';
                qDiv.innerHTML = `
                    <div class="question-meta">
                        <div>
                            <span class="pill-cat">${q.category}</span>
                            <span style="margin-left: 8px; font-weight: 700;">Question ${idx + 1} of ${mockTest.questions.length}</span>
                        </div>
                        <div>
                            <span class="pill-diff">${q.difficulty}</span>
                            <span class="text-muted" style="margin-left: 8px;">Marks: ${q.marks}</span>
                        </div>
                    </div>
                    <div class="question-text">${this.escapeHtml(q.questionText)}</div>
                    <div class="options-grid">
                        <div class="option-box" id="test-opt-${q.id}-A" onclick="app.selectTestOption(${q.id}, 'A')">
                            <span class="opt-letter">A</span>
                            <span>${this.escapeHtml(q.optionA)}</span>
                        </div>
                        <div class="option-box" id="test-opt-${q.id}-B" onclick="app.selectTestOption(${q.id}, 'B')">
                            <span class="opt-letter">B</span>
                            <span>${this.escapeHtml(q.optionB)}</span>
                        </div>
                        <div class="option-box" id="test-opt-${q.id}-C" onclick="app.selectTestOption(${q.id}, 'C')">
                            <span class="opt-letter">C</span>
                            <span>${this.escapeHtml(q.optionC)}</span>
                        </div>
                        <div class="option-box" id="test-opt-${q.id}-D" onclick="app.selectTestOption(${q.id}, 'D')">
                            <span class="opt-letter">D</span>
                            <span>${this.escapeHtml(q.optionD)}</span>
                        </div>
                    </div>
                `;
                container.appendChild(qDiv);
            });

            // Start countdown timer
            this.testTimerSeconds = durationMinutes * 60;
            this.updateTimerDisplay();
            if (this.timerInterval) clearInterval(this.timerInterval);
            this.timerInterval = setInterval(() => {
                app.testTimerSeconds--;
                app.updateTimerDisplay();
                if (app.testTimerSeconds <= 0) {
                    clearInterval(app.timerInterval);
                    app.showAlert('Time is up! Your test has been submitted automatically.', 'info');
                    app.submitActiveTest();
                }
            }, 1000);

            window.scrollTo({ top: 0, behavior: 'smooth' });

        } catch (error) {
            this.showAlert(error.message, 'danger');
        }
    },

    selectTestOption(questionId, optionLetter) {
        this.studentAnswers[questionId] = optionLetter;

        // Visual selection indicator
        ['A', 'B', 'C', 'D'].forEach(letter => {
            const el = document.getElementById(`test-opt-${questionId}-${letter}`);
            if (el) {
                if (letter === optionLetter) {
                    el.classList.add('selected');
                } else {
                    el.classList.remove('selected');
                }
            }
        });
    },

    updateTimerDisplay() {
        const minutes = Math.floor(this.testTimerSeconds / 60);
        const seconds = this.testTimerSeconds % 60;
        const str = `${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;
        const clock = document.getElementById('test-timer');
        if (clock) {
            clock.textContent = str;
            if (this.testTimerSeconds < 60) {
                clock.style.color = '#dc2626';
            } else {
                clock.style.color = '#b45309';
            }
        }
    },

    confirmSubmitTest() {
        const attempted = Object.keys(this.studentAnswers).length;
        const total = this.activeTest ? this.activeTest.questions.length : 0;
        const confirmMsg = `You have answered ${attempted} of ${total} questions. Are you ready to submit and calculate your score?`;
        if (confirm(confirmMsg)) {
            if (this.timerInterval) clearInterval(this.timerInterval);
            this.submitActiveTest();
        }
    },

    async submitActiveTest() {
        if (!this.activeTest) return;

        try {
            const res = await fetch('/api/mocktest/submit', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    testId: this.activeTest.testId,
                    answers: this.studentAnswers
                })
            });

            if (!res.ok) {
                const err = await res.json();
                throw new Error(err.message || 'Submission failed');
            }

            const result = await res.json();
            this.renderTestResult(result);
            await this.refreshCurrentStudent();

        } catch (error) {
            this.showAlert(error.message, 'danger');
        }
    },

    renderTestResult(res) {
        document.getElementById('mock-running-panel').classList.add('hidden');
        document.getElementById('mock-config-panel').classList.add('hidden');
        document.getElementById('mock-result-panel').classList.remove('hidden');

        document.getElementById('res-test-title').textContent = res.title;
        document.getElementById('res-score').textContent = `${res.scoreObtained} / ${res.totalMarks}`;
        document.getElementById('res-percentage').textContent = `${res.percentage}%`;
        document.getElementById('res-grade').textContent = res.grade;
        document.getElementById('res-accuracy').textContent = `${res.correctAnswers} Correct, ${res.wrongAnswers} Wrong, ${res.unattempted} Skipped`;

        // Render question-by-question review
        const reviewContainer = document.getElementById('res-questions-review');
        reviewContainer.innerHTML = '';
        res.questionsReview.forEach((item, idx) => {
            const revCard = document.createElement('div');
            revCard.className = 'review-card';
            const statusClass = item.isCorrect ? 'text-success' : (item.status === 'UNATTEMPTED' ? 'text-muted' : 'text-danger');
            revCard.innerHTML = `
                <div class="flex-between" style="margin-bottom: 6px;">
                    <strong>Q${idx + 1}: ${this.escapeHtml(item.questionText)}</strong>
                    <span class="badge-pill" style="font-size: 0.75rem; background: ${item.isCorrect ? 'var(--success)' : (item.status === 'UNATTEMPTED' ? 'var(--gray-500)' : 'var(--danger)')}; color: white;">
                        ${item.status} (${item.isCorrect ? '+' + item.marks : '0'} pts)
                    </span>
                </div>
                <div style="font-size: 0.85rem; margin-bottom: 6px;">
                    <span>Your Choice: <strong>${item.selectedOption || 'None (Unattempted)'}</strong></span> | 
                    <span>Correct Answer: <strong style="color: var(--success);">${item.correctOption}</strong></span>
                </div>
                <div style="font-size: 0.85rem; background: var(--white); padding: 8px; border-radius: 4px; border: 1px solid var(--gray-200);">
                    <strong>Solution / Explanation:</strong> ${this.escapeHtml(item.explanation || 'No explanation provided.')}
                </div>
            `;
            reviewContainer.appendChild(revCard);
        });

        window.scrollTo({ top: 0, behavior: 'smooth' });
    },

    resetMockTestView() {
        this.activeTest = null;
        document.getElementById('mock-result-panel').classList.add('hidden');
        document.getElementById('mock-running-panel').classList.add('hidden');
        document.getElementById('mock-config-panel').classList.remove('hidden');
        window.scrollTo({ top: 0, behavior: 'smooth' });
    },

    /**
     * Results History & Performance Tracking
     */
    async loadResultsHistory() {
        if (!this.currentUser) return;

        try {
            // Load metrics summary
            const sumRes = await fetch(`/api/results/student/${this.currentUser.id}/summary`);
            if (sumRes.ok) {
                const sum = await sumRes.json();
                document.getElementById('history-total-tests').textContent = sum.totalTests;
                document.getElementById('history-avg-pct').textContent = `${(sum.averagePercentage || 0).toFixed(1)}%`;
                document.getElementById('history-high-pct').textContent = `${(sum.highestPercentage || 0).toFixed(1)}%`;
                document.getElementById('history-accuracy').textContent = `${(sum.overallAccuracy || 0).toFixed(1)}%`;
            }

            // Load results list
            const res = await fetch(`/api/results/student/${this.currentUser.id}`);
            if (!res.ok) throw new Error('Failed to load results');
            const results = await res.json();

            const tbody = document.getElementById('results-table-body');
            tbody.innerHTML = '';

            if (results.length === 0) {
                tbody.innerHTML = '<tr><td colspan="7" class="text-center text-muted" style="padding: 2rem;">No assessments taken yet. Launch a mock test to track your performance!</td></tr>';
                return;
            }

            results.forEach((r, idx) => {
                const dateStr = r.attemptDate ? new Date(r.attemptDate).toLocaleString() : 'N/A';
                let gradeClass = 'grade-b';
                if (r.percentage >= 85) gradeClass = 'grade-a';
                else if (r.percentage >= 60) gradeClass = 'grade-b';
                else if (r.percentage >= 50) gradeClass = 'grade-c';
                else gradeClass = 'grade-f';

                const tr = document.createElement('tr');
                tr.innerHTML = `
                    <td>${idx + 1}</td>
                    <td><strong>${r.testTitle}</strong></td>
                    <td><span class="pill-cat">${r.category}</span></td>
                    <td>${dateStr}</td>
                    <td>${r.scoreObtained} / ${r.totalMarks}</td>
                    <td><strong>${r.percentage}%</strong></td>
                    <td><span class="grade-pill ${gradeClass}">${r.grade}</span></td>
                `;
                tbody.appendChild(tr);
            });

        } catch (error) {
            console.error('Error loading results history:', error);
        }
    },

    /**
     * Placement Eligibility Tracker
     */
    async loadEligibilityTracker() {
        if (!this.currentUser) return;
        await this.refreshCurrentStudent();

        document.getElementById('elig-student-name').textContent = this.currentUser.name;
        document.getElementById('elig-student-cgpa').textContent = this.currentUser.cgpa.toFixed(2);
        document.getElementById('elig-student-backlogs').textContent = this.currentUser.backlogs;
        document.getElementById('elig-student-avg').textContent = `${(this.currentUser.averageScore || 0).toFixed(1)}%`;

        const container = document.getElementById('eligibility-cards-container');
        container.innerHTML = '<div class="text-center text-muted">Evaluating candidate eligibility across recruitment drives...</div>';

        try {
            const res = await fetch(`/api/drives/eligibility/${this.currentUser.id}`);
            if (!res.ok) throw new Error('Failed to evaluate eligibility');
            const drives = await res.json();

            container.innerHTML = '';
            drives.forEach(drive => {
                const card = document.createElement('div');
                card.className = `drive-card ${drive.isEligible ? 'is-eligible' : 'not-eligible'}`;
                card.innerHTML = `
                    <div>
                        <div class="drive-top">
                            <div>
                                <h3 class="company-title">${drive.companyName}</h3>
                                <span class="company-role">${drive.jobRole}</span>
                            </div>
                            <span class="company-package">₹ ${drive.packageLpa} LPA</span>
                        </div>
                        <p class="text-muted" style="font-size: 0.85rem; margin-bottom: 0.75rem;">
                            ${this.escapeHtml(drive.jobDescription || 'Campus Recruitment Drive')}
                        </p>
                        <div class="eligibility-checklist">
                            <div class="check-item ${drive.meetsCgpa ? 'check-pass' : 'check-fail'}">
                                ${drive.meetsCgpa ? '✓' : '✗'} CGPA: Requires ≥ ${drive.minCgpa} (Candidate: ${this.currentUser.cgpa.toFixed(2)})
                            </div>
                            <div class="check-item ${drive.meetsBacklogs ? 'check-pass' : 'check-fail'}">
                                ${drive.meetsBacklogs ? '✓' : '✗'} Backlogs: Max allowed ${drive.maxBacklogs} (Candidate: ${this.currentUser.backlogs})
                            </div>
                            <div class="check-item ${drive.meetsScore ? 'check-pass' : 'check-fail'}">
                                ${drive.meetsScore ? '✓' : '✗'} Test Average: Requires ≥ ${drive.minTestScore}% (Candidate: ${(this.currentUser.averageScore || 0).toFixed(1)}%)
                            </div>
                        </div>
                    </div>
                    <div class="flex-between" style="border-top: 1px solid var(--gray-200); padding-top: 0.75rem; margin-top: 0.5rem;">
                        <span class="text-muted" style="font-size: 0.8rem;">Drive Date: ${drive.driveDate}</span>
                        <span class="badge-pill" style="background: ${drive.isEligible ? 'var(--success)' : 'var(--danger)'}; color: white;">
                            ${drive.isEligible ? 'Eligible to Apply 🎉' : 'Criteria Not Met'}
                        </span>
                    </div>
                `;
                container.appendChild(card);
            });

        } catch (error) {
            container.innerHTML = `<div class="card text-center text-muted">Error loading eligibility: ${error.message}</div>`;
        }
    },

    async runWhatIfSimulator() {
        const cgpa = parseFloat(document.getElementById('whatif-cgpa').value);
        const backlogs = parseInt(document.getElementById('whatif-backlogs').value, 10);

        try {
            const res = await fetch('/api/drives/check-hypothetical', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ cgpa, backlogs })
            });

            if (!res.ok) throw new Error('Simulation failed');
            const list = await res.json();

            const box = document.getElementById('whatif-results-box');
            box.classList.remove('hidden');
            box.innerHTML = `
                <h4>Simulation Results for CGPA: ${cgpa} & Backlogs: ${backlogs}</h4>
                <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 0.75rem; margin-top: 0.75rem;">
                    ${list.map(d => `
                        <div style="background: var(--white); padding: 10px; border-radius: 6px; border: 1px solid var(--gray-200); display: flex; justify-content: space-between; align-items: center;">
                            <div>
                                <strong>${d.companyName}</strong>
                                <div style="font-size: 0.8rem; color: var(--gray-500);">${d.jobRole}</div>
                            </div>
                            <span class="badge-pill" style="background: ${d.isEligible ? 'var(--success)' : 'var(--danger)'}; color: white;">
                                ${d.isEligible ? 'Eligible' : 'Not Eligible'}
                            </span>
                        </div>
                    `).join('')}
                </div>
            `;
        } catch (error) {
            this.showAlert(error.message, 'danger');
        }
    },

    /**
     * Admin Panel Logic
     */
    async loadAdminPanel() {
        try {
            // Load stats
            const statsRes = await fetch('/api/admin/stats');
            if (statsRes.ok) {
                const s = await statsRes.json();
                document.getElementById('adm-total-questions').textContent = s.totalQuestions;
                document.getElementById('adm-total-students').textContent = s.totalStudents;
                document.getElementById('adm-total-tests').textContent = s.totalTestsTaken;
                document.getElementById('adm-total-drives').textContent = s.totalDrives;
            }

            // Load questions table
            const qRes = await fetch('/api/admin/questions');
            if (qRes.ok) {
                const questions = await qRes.json();
                const tbody = document.getElementById('adm-questions-table');
                tbody.innerHTML = '';
                questions.forEach((q, idx) => {
                    const tr = document.createElement('tr');
                    tr.innerHTML = `
                        <td>${idx + 1}</td>
                        <td><span class="pill-cat">${q.category}</span></td>
                        <td>${q.subCategory}</td>
                        <td>${this.escapeHtml(q.questionText.substring(0, 75))}...</td>
                        <td><strong style="color: var(--success);">${q.correctOption}</strong></td>
                        <td><span class="pill-diff">${q.difficulty}</span></td>
                        <td>
                            <button class="btn btn-sm btn-outline-danger" onclick="app.deleteQuestion(${q.id})">Delete</button>
                        </td>
                    `;
                    tbody.appendChild(tr);
                });
            }

            // Load drives table
            const dRes = await fetch('/api/drives');
            if (dRes.ok) {
                const drives = await dRes.json();
                const tbody = document.getElementById('adm-drives-table');
                tbody.innerHTML = '';
                drives.forEach(d => {
                    const tr = document.createElement('tr');
                    tr.innerHTML = `
                        <td><strong>${d.companyName}</strong></td>
                        <td>${d.jobRole}</td>
                        <td>₹ ${d.packageLpa} LPA</td>
                        <td>${d.minCgpa}</td>
                        <td>${d.maxBacklogs}</td>
                        <td>${d.minTestScore}%</td>
                        <td><span class="badge-pill bg-success">${d.status}</span></td>
                        <td>
                            <button class="btn btn-sm btn-outline-danger" onclick="app.deleteDrive(${d.id})">Delete</button>
                        </td>
                    `;
                    tbody.appendChild(tr);
                });
            }

        } catch (error) {
            console.error('Error loading admin panel:', error);
        }
    },

    openAddQuestionModal() {
        document.getElementById('form-q-id').value = '';
        document.getElementById('question-form').reset();
        document.getElementById('modal-q-title').textContent = 'Add New Question to Bank';
        document.getElementById('question-modal').classList.remove('hidden');
    },

    openAddDriveModal() {
        document.getElementById('drive-form').reset();
        document.getElementById('drive-modal').classList.remove('hidden');
    },

    closeModal(id) {
        document.getElementById(id).classList.add('hidden');
    },

    async handleSaveQuestion(event) {
        event.preventDefault();
        const payload = {
            category: document.getElementById('form-q-category').value,
            subCategory: document.getElementById('form-q-sub').value.trim(),
            questionText: document.getElementById('form-q-text').value.trim(),
            optionA: document.getElementById('form-q-optA').value.trim(),
            optionB: document.getElementById('form-q-optB').value.trim(),
            optionC: document.getElementById('form-q-optC').value.trim(),
            optionD: document.getElementById('form-q-optD').value.trim(),
            correctOption: document.getElementById('form-q-correct').value,
            difficulty: document.getElementById('form-q-difficulty').value,
            marks: parseInt(document.getElementById('form-q-marks').value, 10),
            explanation: document.getElementById('form-q-explanation').value.trim()
        };

        try {
            const res = await fetch('/api/admin/questions', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            if (!res.ok) {
                const err = await res.json();
                throw new Error(err.message || 'Error saving question');
            }

            this.showAlert('Question added to bank successfully!', 'success');
            this.closeModal('question-modal');
            this.loadAdminPanel();
        } catch (error) {
            this.showAlert(error.message, 'danger');
        }
    },

    async deleteQuestion(id) {
        if (!confirm('Are you sure you want to delete this question?')) return;
        try {
            const res = await fetch(`/api/admin/questions/${id}`, { method: 'DELETE' });
            if (res.ok) {
                this.showAlert('Question deleted successfully.', 'success');
                this.loadAdminPanel();
            }
        } catch (error) {
            this.showAlert(error.message, 'danger');
        }
    },

    async handleSaveDrive(event) {
        event.preventDefault();
        const payload = {
            companyName: document.getElementById('form-d-company').value.trim(),
            jobRole: document.getElementById('form-d-role').value.trim(),
            packageLpa: parseFloat(document.getElementById('form-d-pkg').value),
            minCgpa: parseFloat(document.getElementById('form-d-cgpa').value),
            maxBacklogs: parseInt(document.getElementById('form-d-backlogs').value, 10),
            minTestScore: parseFloat(document.getElementById('form-d-score').value),
            driveDate: document.getElementById('form-d-date').value,
            status: 'ACTIVE',
            jobDescription: document.getElementById('form-d-desc').value.trim()
        };

        try {
            const res = await fetch('/api/admin/drives', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            if (!res.ok) {
                const err = await res.json();
                throw new Error(err.message || 'Error saving recruitment drive');
            }

            this.showAlert('Placement recruitment drive added successfully!', 'success');
            this.closeModal('drive-modal');
            this.loadAdminPanel();
        } catch (error) {
            this.showAlert(error.message, 'danger');
        }
    },

    async deleteDrive(id) {
        if (!confirm('Are you sure you want to delete this placement drive?')) return;
        try {
            const res = await fetch(`/api/admin/drives/${id}`, { method: 'DELETE' });
            if (res.ok) {
                this.showAlert('Placement drive deleted.', 'success');
                this.loadAdminPanel();
            }
        } catch (error) {
            this.showAlert(error.message, 'danger');
        }
    },

    /**
     * Helpers & Utilities
     */
    showAlert(msg, type = 'info') {
        const banner = document.getElementById('alert-banner');
        banner.className = `alert-banner alert-${type}`;
        banner.textContent = msg;
        banner.classList.remove('hidden');
        setTimeout(() => banner.classList.add('hidden'), 5000);
    },

    escapeHtml(str) {
        if (!str) return '';
        return str
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;')
            .replace(/'/g, '&#039;');
    }
};

// Start application when DOM is ready
document.addEventListener('DOMContentLoaded', () => {
    app.init();
});
