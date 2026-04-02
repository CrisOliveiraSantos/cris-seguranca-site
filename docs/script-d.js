const revealTargets = document.querySelectorAll('.reveal');

const revealObserver = new IntersectionObserver(
  (entries) => {
    entries.forEach((entry) => {
      if (entry.isIntersecting) {
        entry.target.classList.add('in-view');
        revealObserver.unobserve(entry.target);
      }
    });
  },
  {
    threshold: 0.12,
    rootMargin: '0px 0px -40px 0px'
  }
);

revealTargets.forEach((target) => revealObserver.observe(target));

const motionLines = document.querySelectorAll('.glass-line, .glass-strip, .glass-story');

motionLines.forEach((element) => {
  element.addEventListener('pointermove', (event) => {
    const rect = element.getBoundingClientRect();
    const x = ((event.clientX - rect.left) / rect.width) * 100;
    const y = ((event.clientY - rect.top) / rect.height) * 100;
    element.style.setProperty('--mx', `${x}%`);
    element.style.setProperty('--my', `${y}%`);
  });
});

const planToggles = document.querySelectorAll('.plan-toggle');

const closePlanDetail = (toggle, detail, planRow, immediate = false) => {
  toggle.setAttribute('aria-expanded', 'false');
  planRow?.classList.remove('is-open');

  if (immediate) {
    detail.classList.remove('is-visible');
    detail.hidden = true;
    return;
  }

  detail.classList.remove('is-visible');

  const handleTransitionEnd = (event) => {
    if (event.propertyName !== 'max-height') {
      return;
    }

    if (toggle.getAttribute('aria-expanded') === 'false') {
      detail.hidden = true;
    }

    detail.removeEventListener('transitionend', handleTransitionEnd);
  };

  detail.addEventListener('transitionend', handleTransitionEnd);
};

const openPlanDetail = (toggle, detail, planRow) => {
  toggle.setAttribute('aria-expanded', 'true');
  detail.hidden = false;

  requestAnimationFrame(() => {
    requestAnimationFrame(() => {
      detail.classList.add('is-visible');
      planRow?.classList.add('is-open');
    });
  });
};

planToggles.forEach((toggle) => {
  toggle.addEventListener('click', () => {
    const detailId = toggle.getAttribute('aria-controls');
    const detail = document.getElementById(detailId);
    const planRow = toggle.closest('.plan-row');
    const isOpen = toggle.getAttribute('aria-expanded') === 'true';

    if (!detail) {
      return;
    }

    planToggles.forEach((otherToggle) => {
      const otherDetailId = otherToggle.getAttribute('aria-controls');
      const otherDetail = document.getElementById(otherDetailId);
      const otherPlanRow = otherToggle.closest('.plan-row');

      if (!otherDetail) {
        return;
      }

      if (otherToggle === toggle && isOpen) {
        closePlanDetail(otherToggle, otherDetail, otherPlanRow);
        return;
      }

      closePlanDetail(otherToggle, otherDetail, otherPlanRow, otherToggle !== toggle);
    });

    if (!isOpen) {
      openPlanDetail(toggle, detail, planRow);
    }
  });
});
