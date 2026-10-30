CREATE TABLE cases (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       title VARCHAR(100) NOT NULL,
                       case_order INT NOT NULL UNIQUE,
                       briefing TEXT NOT NULL,
                       victim_name VARCHAR(100) NOT NULL,
                       location VARCHAR(200) NOT NULL,
                       estimated_death_time VARCHAR(100) NOT NULL,
                       cause_of_death VARCHAR(200) NOT NULL,
                       scene_image_url VARCHAR(500),
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE suspects (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          case_id BIGINT NOT NULL,
                          name VARCHAR(100) NOT NULL,
                          role_name VARCHAR(100) NOT NULL,
                          initial_statement TEXT NOT NULL,
                          display_order INT NOT NULL,
                          UNIQUE (case_id, display_order),
                          FOREIGN KEY (case_id) REFERENCES cases (id)
);

CREATE TABLE questions (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           suspect_id BIGINT NOT NULL,
                           question_text VARCHAR(500) NOT NULL,
                           display_order INT NOT NULL,
                           UNIQUE (suspect_id, display_order),
                           FOREIGN KEY (suspect_id) REFERENCES suspects (id)
);

CREATE TABLE statements (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            question_id BIGINT NOT NULL UNIQUE,
                            answer_text TEXT NOT NULL,
                            FOREIGN KEY (question_id) REFERENCES questions (id)
);

CREATE TABLE evidences (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           case_id BIGINT NOT NULL,
                           name VARCHAR(100) NOT NULL,
                           description TEXT NOT NULL,
                           image_url VARCHAR(500),
                           display_order INT NOT NULL,
                           UNIQUE (case_id, display_order),
                           FOREIGN KEY (case_id) REFERENCES cases (id)
);

CREATE TABLE case_hints (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            case_id BIGINT NOT NULL,
                            content TEXT NOT NULL,
                            hint_order INT NOT NULL,
                            UNIQUE (case_id, hint_order),
                            FOREIGN KEY (case_id) REFERENCES cases (id)
);

CREATE TABLE case_solutions (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                case_id BIGINT NOT NULL UNIQUE,
                                culprit_suspect_id BIGINT NOT NULL,
                                truth_summary TEXT NOT NULL,
                                FOREIGN KEY (case_id) REFERENCES cases (id),
                                FOREIGN KEY (culprit_suspect_id) REFERENCES suspects (id)
);

CREATE TABLE case_solution_evidences (
                                         case_solution_id BIGINT NOT NULL,
                                         evidence_id BIGINT NOT NULL,
                                         PRIMARY KEY (case_solution_id, evidence_id),
                                         FOREIGN KEY (case_solution_id) REFERENCES case_solutions (id),
                                         FOREIGN KEY (evidence_id) REFERENCES evidences (id)
);

CREATE TABLE solution_keyword_groups (
                                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                         case_solution_id BIGINT NOT NULL,
                                         group_name VARCHAR(100) NOT NULL,
                                         display_order INT NOT NULL,
                                         UNIQUE (case_solution_id, display_order),
                                         FOREIGN KEY (case_solution_id) REFERENCES case_solutions (id)
);

CREATE TABLE solution_keywords (
                                   id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                   keyword_group_id BIGINT NOT NULL,
                                   keyword_text VARCHAR(100) NOT NULL,
                                   UNIQUE (keyword_group_id, keyword_text),
                                   FOREIGN KEY (keyword_group_id) REFERENCES solution_keyword_groups (id)
);

CREATE TABLE anonymous_players (
                                   id CHAR(36) PRIMARY KEY,
                                   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE game_sessions (
                               id CHAR(36) PRIMARY KEY,
                               player_id CHAR(36) NOT NULL,
                               case_id BIGINT NOT NULL,
                               status VARCHAR(20) NOT NULL DEFAULT 'PLAYING',
                               score INT NOT NULL DEFAULT 100,
                               hint_count INT NOT NULL DEFAULT 0,
                               wrong_answer_count INT NOT NULL DEFAULT 0,
                               submitted_suspect_id BIGINT,
                               submitted_evidence_id BIGINT,
                               submitted_truth TEXT,
                               grade CHAR(1),
                               solved_at TIMESTAMP NULL,
                               created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                               UNIQUE (player_id, case_id),
                               FOREIGN KEY (player_id) REFERENCES anonymous_players (id),
                               FOREIGN KEY (case_id) REFERENCES cases (id),
                               FOREIGN KEY (submitted_suspect_id) REFERENCES suspects (id),
                               FOREIGN KEY (submitted_evidence_id) REFERENCES evidences (id)
);

CREATE TABLE interrogation_history (
                                       session_id CHAR(36) NOT NULL,
                                       question_id BIGINT NOT NULL,
                                       asked_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                       PRIMARY KEY (session_id, question_id),
                                       FOREIGN KEY (session_id) REFERENCES game_sessions (id),
                                       FOREIGN KEY (question_id) REFERENCES questions (id)
);

CREATE TABLE hint_history (
                              session_id CHAR(36) NOT NULL,
                              hint_id BIGINT NOT NULL,
                              used_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              PRIMARY KEY (session_id, hint_id),
                              FOREIGN KEY (session_id) REFERENCES game_sessions (id),
                              FOREIGN KEY (hint_id) REFERENCES case_hints (id)
);

INSERT INTO cases (id, title, case_order, briefing, victim_name, location, estimated_death_time, cause_of_death)
VALUES (1, 'CASE 01 — 식탁 위의 거짓말', 1,
        '토요일 오전 10시 14분, 유명 요리 연구가 백도윤이 자신의 산속 별장에서 숨진 채 발견되었다.\n\n피해자는 식탁 의자에 앉은 상태였으며, 사인은 목 졸림으로 추정된다. 사망 추정 시각은 수요일 오후부터 목요일 오전 사이다.\n\n현장 식탁에는 조리한 지 반나절이 지나지 않은 것으로 보이는 음식들이 차려져 있었다. 현관문과 창문에는 강제 침입 흔적이 없다.\n\n사건과 관련된 인물 네 명의 진술을 듣고, 현장의 증거를 확인하라.',
        '백도윤', '백도윤의 산속 별장', '수요일 오후 ~ 목요일 오전', '목 졸림');

INSERT INTO suspects (id, case_id, name, role_name, initial_statement, display_order) VALUES
                                                                                          (1, 1, '오만수', '별장 관리인', '저는 매주 토요일 오전에 선생님께 요리 재료를 전달하러 옵니다. 오늘도 평소처럼 왔는데 아무 대답이 없었습니다.\n비상 상황에만 쓰라고 맡겨 둔 열쇠로 들어가 보니, 선생님이 식탁에 앉은 채 움직이지 않고 계셨습니다. 저는 토요일 외에는 이 별장에 오지 않습니다.', 1),
                                                                                          (2, 1, '최민석', '요리 프로그램 연출가', '수요일 오전에 프로그램 문제로 백도윤 선생님을 찾아갔습니다. 노크했지만 대답이 없었어요. 그래도 안에서 커피 냄새가 났고, 작업에 집중하고 계신 줄 알았습니다. 괜히 방해하고 싶지 않아 돌아왔습니다.\n\n그리고 두 분 부부 사이는 좋지 않았습니다. 촬영 현장에서도 이혼과 돈 문제로 다투는 모습을 여러 번 봤습니다.', 2),
                                                                                          (3, 1, '이서연', '피해자의 부인', '수요일 오후에 남편을 찾아간 것은 사실입니다. 이혼과 돈 문제로 할 이야기가 있었어요. 하지만 남편은 문을 열어주지 않았습니다. 제가 싫어서 일부러 피한 거라고 생각했고, 화가 나서 호텔로 돌아왔습니다.\n\n목요일 오전에는 호텔 로비에서 강태욱 편집자를 만났습니다. 그분은 남편을 만나러 별장에 갔는데, 안에서 코 고는 소리가 들려 그냥 돌아왔다고 했습니다.', 3),
                                                                                          (4, 1, '강태욱', '요리만화 편집자', '저는 목요일 아침에 원고 일정 때문에 별장에 갔습니다. 문을 열지는 않았습니다. 안에서 코 고는 소리가 들렸고, 피곤해서 주무시는 줄 알았습니다. 굳이 깨울 필요는 없다고 생각해서 돌아왔습니다.', 4);

INSERT INTO questions (id, suspect_id, question_text, display_order) VALUES
                                                                         (1, 1, '평일에는 정말 별장에 오지 않았습니까?', 1),
                                                                         (2, 1, '비상 열쇠는 누구나 사용할 수 있었습니까?', 2),
                                                                         (3, 1, '피해자와 개인적인 갈등은 없었습니까?', 3),
                                                                         (4, 2, '프로그램 문제란 무엇이었습니까?', 1),
                                                                         (5, 2, '산속 별장까지 왔다가 왜 그냥 돌아갔습니까?', 2),
                                                                         (6, 2, '피해자 부부의 관계에 대해 아는 것이 있습니까?', 3),
                                                                         (7, 3, '수요일에 별장에 간 이유는 무엇입니까?', 1),
                                                                         (8, 3, '문을 열지 않았는데도 왜 돌아갔습니까?', 2),
                                                                         (9, 3, '호텔 정전은 언제 일어났습니까?', 3),
                                                                         (10, 3, '목요일에 만난 편집자는 무슨 말을 했습니까?', 4),
                                                                         (11, 4, '목요일에 별장을 방문한 목적은 무엇입니까?', 1),
                                                                         (12, 4, '피해자가 실제로 살아 있는 모습을 보았습니까?', 2),
                                                                         (13, 4, '피해자와 원고 문제로 다툰 적이 있습니까?', 3),
                                                                         (14, 4, '목요일 이후에도 별장에 간 적이 있습니까?', 4);

INSERT INTO statements (question_id, answer_text) VALUES
                                                      (1, '그렇습니다. 저는 매주 토요일 오전에만 재료를 전달하러 옵니다. 평일에는 별장 근처에도 가지 않습니다.'),
                                                      (2, '제가 관리하고 있습니다. 화재나 누수 같은 비상 상황 외에는 쓰지 않습니다. 오늘도 선생님이 대답하지 않으셔서 처음 사용했습니다.'),
                                                      (3, '전혀 없습니다. 저는 재료 전달과 시설 관리만 맡고 있을 뿐입니다.'),
                                                      (4, '방송국에서 요리 프로그램 폐지를 결정했습니다. 선생님께 직접 말씀드리기 어려운 이야기였습니다.'),
                                                      (5, '노크해도 대답은 없었지만, 안에서 커피 냄새가 났습니다. 작업에 집중하고 계신 줄 알고 방해하지 않으려 했습니다.'),
                                                      (6, '두 분은 촬영 현장에서도 이혼과 돈 문제로 자주 다투셨습니다. 관계가 좋았다고 말하기는 어렵습니다.'),
                                                      (7, '이혼과 재산 문제를 정리하려고 갔습니다. 별장 문제도 포함되어 있었습니다.'),
                                                      (8, '남편은 화가 나면 저를 피하곤 했습니다. 그날도 저와 이야기하기 싫어서 문을 열지 않는 것이라고 생각했습니다.'),
                                                      (9, '수요일 저녁이었습니다. 폭우가 내린 뒤 호텔 두꺼비집이 내려가 한동안 정전 소동이 있었습니다.'),
                                                      (10, '강태욱 편집자분은 목요일 오전에 남편을 보러 갔는데, 안에서 코를 골며 자고 있어 깨우지 않고 돌아왔다고 했습니다.'),
                                                      (11, '연재 원고 일정이 계속 밀리고 있어서 이야기하려 했습니다.'),
                                                      (12, '아니요. 문을 열지는 않았습니다. 안에서 코 고는 소리만 들었습니다.'),
                                                      (13, '일정 때문에 언성이 높아진 적은 있습니다. 하지만 작가와 편집자 사이에 흔한 일입니다.'),
                                                      (14, '없습니다. 목요일 아침 이후에는 제 사무실에 있었습니다.');

INSERT INTO evidences (id, case_id, name, description, display_order) VALUES
                                                                          (1, 1, '식탁 위의 음식', '식탁에는 크림 버섯 수프, 허브 치킨구이, 레드와인 배 타르트가 차려져 있다. 음식은 토요일 오전 기준, 조리된 지 반나절이 지나지 않은 상태로 보인다.', 1),
                                                                          (2, 1, '부인의 요리책', '책 제목은 《비 오는 날의 식탁》이다. 책갈피가 꽂힌 페이지에는 새싹 샐러드, 크림 버섯 수프, 허브 치킨구이, 레드와인 배 타르트로 이루어진 4코스 메뉴가 적혀 있다.', 2),
                                                                          (3, 1, '주방 전자레인지', '전원은 정상적으로 작동한다. 시계가 00:00에서 점멸 중이며, 기기 외부와 내부에 고장 흔적은 없다.', 3),
                                                                          (4, 1, '호텔 정전 안내문', '수요일 저녁 폭우로 인해 호텔 전력 공급에 일시적인 문제가 발생했다는 안내문이다.', 4),
                                                                          (5, 1, '별장 비상 열쇠', '별장 관리인 오만수가 보관 중인 비상 열쇠다. 화재나 누수 같은 비상 상황에만 사용하도록 피해자가 맡긴 열쇠이며, 관리인은 토요일 시신 발견 당시 사용했다고 진술했다.', 5),
                                                                          (6, 1, '찢어진 이혼 합의서 초안', '피해자 서재 휴지통에서 발견됐다. 재산 분할과 별장 소유권 문제가 적혀 있으며, 이서연의 서명란은 비어 있다.', 6),
                                                                          (7, 1, '프로그램 폐지 통보 메일', '수요일 오전 최민석 연출가에게 전달된 방송국 메일이다. 피해자가 출연하는 요리 프로그램의 폐지가 결정되었으며, 연출가가 피해자에게 직접 설명해야 한다는 내용이 담겨 있다.', 7);

INSERT INTO case_hints (id, case_id, content, hint_order) VALUES
                                                              (1, 1, '수요일 저녁의 폭우와 호텔 정전, 그리고 주방 전자레인지의 시간을 함께 살펴보세요.', 1),
                                                              (2, 1, '토요일 오전에 발견된 음식이 반나절 이내에 조리된 것이라면, 누군가는 금요일 밤에 식탁을 준비했을 수 있습니다.', 2),
                                                              (3, 1, '요리책에 적힌 메뉴와 식탁 위에 놓인 음식을 하나씩 비교해 보세요.', 3);

INSERT INTO case_solutions (id, case_id, culprit_suspect_id, truth_summary) VALUES
    (1, 1, 4, '수요일 오전, 요리 프로그램 연출가 최민석은 프로그램 폐지 문제를 이야기하기 위해 백도윤의 별장을 찾았다. 백도윤은 안에서 커피를 마시며 작업 중이었지만, 불편한 이야기를 피하고 싶어 문을 열어주지 않았다. 최민석은 커피 냄새를 맡고 백도윤이 일에 집중하고 있다고 생각한 채 돌아갔다.\n\n수요일 오후, 백도윤의 부인 이서연도 이혼과 재산 문제를 이야기하기 위해 별장을 방문했다. 하지만 백도윤은 아내와 대화하고 싶지 않아 일부러 문을 열어주지 않았다. 이서연은 남편이 자신을 피한다고 생각해 화가 난 채 호텔로 돌아갔다.\n\n이서연이 떠난 뒤, 요리만화 편집자 강태욱이 별장을 찾았다. 강태욱은 밀린 연재 원고와 계약 문제를 두고 백도윤과 말다툼했다. 감정이 격해진 끝에 그는 우발적으로 백도윤의 목을 졸라 살해했다.\n\n그날 밤 폭우로 호텔 일대에 정전이 발생했다. 별장에도 같은 정전이 발생해 차단기가 내려갔지만, 이미 백도윤은 사망한 뒤였다. 따라서 별장의 전력을 다시 복구할 사람은 없었다.\n\n목요일 오전, 강태욱은 호텔 로비에서 이서연을 만났다. 그는 백도윤이 코를 골며 자고 있어 깨우지 않고 돌아왔다고 말했다. 하지만 강태욱은 목요일에 별장을 찾지 않았다. 자신이 수요일에 백도윤을 만났다는 사실과 사망 시점을 숨기기 위해, 피해자가 목요일까지 살아 있었던 것처럼 거짓말한 것이다.\n\n금요일이 지나도록 시신이 발견되지 않자 강태욱은 불안해졌다. 그는 자신의 거짓말이 의심받을 것을 걱정해 금요일 밤 다시 별장으로 돌아왔다. 그곳에서 생각지도 못한 사실을 깨달았다. 수요일 밤 폭우로 별장에도 정전이 발생해 차단기가 내려가 있었던 것이다.\n\n강태욱은 차단기를 다시 올렸지만, 이미 냉장고 속 식재료는 모두 상해 있었다. 그는 백도윤이 최근까지 살아 있었던 것처럼 보이도록 식탁을 꾸미기로 했다. 이서연의 요리책에 나온 4코스 메뉴를 따라 상한 재료로 크림 버섯 수프, 허브 치킨구이, 레드와인 배 타르트를 만들어 식탁에 차려 놓았다.\n\n그러나 날것으로 먹는 새싹 샐러드는 상한 채소를 조리로 감출 수 없었기에 만들 수 없었다. 요리책의 4코스 메뉴 가운데 샐러드만 빠진 식탁, 정전 후 초기화된 전자레인지 시계, 그리고 강태욱의 거짓 진술은 모두 그가 금요일 밤 현장을 조작했다는 사실을 가리킨다. 범인은 강태욱이다.');

INSERT INTO case_solution_evidences (case_solution_id, evidence_id) VALUES
                                                                        (1, 1), (1, 2), (1, 3);

INSERT INTO solution_keyword_groups (id, case_solution_id, group_name, display_order) VALUES
                                                                                          (1, 1, '정전', 1),
                                                                                          (2, 1, '식재료 변질', 2),
                                                                                          (3, 1, '샐러드', 3);

INSERT INTO solution_keywords (keyword_group_id, keyword_text) VALUES
                                                                   (1, '정전'), (1, '차단기'), (1, '두꺼비집'), (1, '전기 끊김'),
                                                                   (2, '상한'), (2, '상했다'), (2, '썩은'), (2, '썩었다'), (2, '변질'), (2, '부패'), (2, '냉장고가 꺼짐'),
                                                                   (3, '샐러드'), (3, '생채소'), (3, '날것');
